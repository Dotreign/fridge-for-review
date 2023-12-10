package com.example.filter;

import com.example.config.CustomUserDetailsService;
import com.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Objects;
import java.util.function.Function;

@Component
public class AuthFilter implements WebFilter {

    private CustomUserDetailsService customUserDetailsService;
    private JwtUtils jwtUtils;

    @Autowired
    public AuthFilter(CustomUserDetailsService customUserDetailsService, JwtUtils jwtUtils) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) {
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("accessToken"))
                .map(HttpCookie::getValue)
                .flatMap(jwtToken -> jwtUtils.parseJwt(jwtToken)
                        .flatMap(claimsJws -> jwtUtils.extractSubject(Mono.just(claimsJws))
                                .flatMap(email -> customUserDetailsService.findByUsername(email)
                                        .flatMap(userDetails -> jwtUtils.validateToken(jwtToken)
                                                .filter(valid -> valid)
                                                .map(valid -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))
                                        )
                                )
                        )
                )
                .map(SecurityContextImpl::new)
                .map(securityContext -> filterChain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))))
                .defaultIfEmpty(filterChain.filter(exchange))
                .flatMap(Function.identity());
    }
}
