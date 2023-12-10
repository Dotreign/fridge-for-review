package com.example.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends CorsWebFilter {

    public CorsFilter() {
        super(corsConfigurationSource());
    }

    private static UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Only allow http://frontend:9000 origin
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost"));

        // Allow all methods
        configuration.setAllowedMethods(Collections.singletonList("*"));

        // Allow all headers
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Apply the CORS configuration to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}