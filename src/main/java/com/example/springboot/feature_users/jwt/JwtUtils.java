package com.example.springboot.feature_users.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private static final String SECRET =
            "yourSuperSecretKeyyourSuperSecretKeyyourSuperSecretKeyyourSuperSecretKey";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, List<String> roles, String userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Adding roles as a claim
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        return claimsResolver.apply(claims);
    }
}



