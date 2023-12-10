package com.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtUtils {

    private String secret = "RCJEFOwZw0YYAnyYePGqQ9g3HdpkhcWN";

    private SecretKey KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    private final Instant expirationAccess = Instant.now().plusSeconds(86400);



    public String generateAccessToken(String subject, List<String> authorities) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("authorities", authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expirationAccess))
                .signWith(KEY)
                .compact();
    }

    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Date expiration = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody().getExpiration();
                return !expiration.before(Date.from(Instant.now()));
            } catch (Exception e) {
                return false;
            }
        });
    }

    public Mono<Jws<Claims>> parseJwt(String jwtToken) {
        return Mono.fromCallable(() -> Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(jwtToken));
    }

    public Mono<String> extractSubject(Mono<Jws<Claims>> claims) {
        return claims.map(c -> c.getBody().getSubject());
    }
}