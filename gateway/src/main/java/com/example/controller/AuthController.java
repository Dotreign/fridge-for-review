package com.example.controller;

import com.example.entity.AuthRequest;
import com.example.entity.AuthResponse;
import com.example.entity.RegisterRequest;
import com.example.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody Mono<AuthRequest> requestMono, ServerWebExchange ex) {
        return requestMono.doOnNext(request -> logger.info(request.getEmail()))
                .flatMap(request -> authService.login(request, ex)
                        .flatMap(authResponse -> Mono.just(ResponseEntity.ok().body(authResponse)))
                        .onErrorResume(e -> {
                            logger.error(e.getMessage());
                            AuthResponse authResponse = new AuthResponse();
                            authResponse.setSuccess(false);
                            authResponse.setMessage(e.getMessage());
                            return Mono.just(ResponseEntity.badRequest().body(authResponse));
                        }));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody Mono<RegisterRequest> request){
        return request
                .flatMap(authService::register) // предполагаем, что authService.register() возвращает Mono<Void>
                .thenReturn(ResponseEntity.ok("User registered successfully"))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("Pong");
    }

}
