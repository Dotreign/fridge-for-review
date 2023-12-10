package com.example.service;

import com.example.config.CustomUserDetailsService;
import com.example.entity.*;
import com.example.repository.UserEntityRepository;
import com.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class AuthService {

    UserEntityRepository repository;
    BCryptPasswordEncoder encoder;
    JwtUtils jwtUtils;
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    public AuthService(UserEntityRepository repository,
                       BCryptPasswordEncoder encoder,
                       JwtUtils jwtUtils,
                       CustomUserDetailsService customUserDetailsService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    public Mono<UserEntity> register(RegisterRequest request) {
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        return repository.save(user);
    }

    public Mono<AuthResponse> login(AuthRequest request, ServerWebExchange exchange) {

        if (request == null || exchange == null) {
            throw new IllegalArgumentException("request and exchange cannot be null");
        }

        String email = request.getEmail();
        String password = request.getPassword();

        if (email == null || password == null) {
            throw new IllegalArgumentException("email and password cannot be null");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        return this.authenticationManager().authenticate(authenticationToken)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("INVALID_CREDENTIALS"))))
                .cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(authentication -> {
                    String accessToken = jwtUtils.generateAccessToken(request.getEmail(), authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
                    ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                            .path("/")
                            .httpOnly(true)
                            .secure(true)
                            .build();
                    exchange.getResponse().addCookie(accessCookie);
                    Mono<UserEntity> user = this.repository.findByEmail(request.getEmail());
                    AuthResponse response = new AuthResponse();
                    UserData userData = new UserData();
                    userData.setName(user.block().getName());
                    response.setUserData(userData);
                    response.setSuccess(true);
                    response.setMessage("Login successful");
                    return Mono.just(response);
                });
    }

    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(this.customUserDetailsService);
        manager.setPasswordEncoder(this.encoder);
        return manager;
    }

}
