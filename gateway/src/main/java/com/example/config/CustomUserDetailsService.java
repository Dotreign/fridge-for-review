package com.example.config;

import com.example.entity.UserEntity;
import com.example.repository.UserEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    UserEntityRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);


    @Autowired
    public CustomUserDetailsService(UserEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return repository.findByEmail(email)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User not found"))))
                .map(credential -> {
                    UserDetails user = User.builder()
                            .username(credential.getName())
                            .password(credential.getPassword())
                            .roles(credential.getRole().name())
                            .build();
                    logger.info("Found user: {}", user);
                    return user;
                });
    }
}
