package com.example.config;

import com.example.filter.AuthFilter;
import com.example.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private CorsFilter corsFilter;
    private AuthFilter authFilter;

    @Autowired
    public SecurityConfig(CorsFilter corsFilter, AuthFilter authFilter) {
        this.authFilter = authFilter;
        this.corsFilter = corsFilter;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterBefore(corsFilter, SecurityWebFiltersOrder.CORS)
                .addFilterAfter(authFilter, SecurityWebFiltersOrder.CORS)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange((requests) -> requests
                        .pathMatchers("/auth/register", "/auth/login").permitAll()
                        .anyExchange().authenticated())
                .build();
    }

}
