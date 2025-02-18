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

    /**
     * Generates a JWT token with the given username, roles, and user ID. The token is signed with a
     * secret key and contains the following claims: - subject (username) - roles - issued at time -
     * expiration time (1 hour)
     *
     * @param username The username of the user to associate with the token.
     * @param roles The list of roles the user has.
     * @param userId The ID of the user (not used in the current implementation but can be added to
     *     the token).
     * @return The generated JWT token as a string.
     */
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

    /**
     * Validates the provided JWT token by checking if the username matches the one in the token and
     * if the token has not expired.
     *
     * @param token The JWT token to validate.
     * @param username The username to compare with the token's subject.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    /**
     * Extracts the list of roles from the JWT token.
     *
     * @param token The JWT token from which to extract the roles.
     * @return A list of roles contained in the token.
     */
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token The JWT token from which to extract the username.
     * @return The username contained in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks whether the provided JWT token is expired.
     *
     * @param token The JWT token to check.
     * @return True if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token The JWT token to extract the claim from.
     * @param claimsResolver A function to extract the specific claim from the JWT's claims.
     * @param <T> The type of the claim to extract.
     * @return The extracted claim.
     */
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
