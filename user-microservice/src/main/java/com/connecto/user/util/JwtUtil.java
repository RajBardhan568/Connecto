package com.connecto.user.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // IMPORTANT: Use a secure, long, base64-encoded key in production.
    @Value("${jwt.secret}")
    private String secret;

    // Token Validity: 1 hour (3600 * 1000 milliseconds)
    private static final long EXPIRATION_TIME = 3600000; 

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT upon successful user authentication.
     * @param userId The ID of the authenticated user.
     * @param email The email address of the authenticated user.
     * @return The generated JWT string.
     */
    public String generateToken(Long userId, String email) {
        // Defines the token claims (data payload)
        return Jwts.builder()
                .setSubject(email) // The principal subject (usually unique identifier)
                .claim("userId", userId) // Custom claim for user ID
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey()) // Sign the token with our secret key
                .compact();
    }
}