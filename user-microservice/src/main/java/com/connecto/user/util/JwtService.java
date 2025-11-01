package com.connecto.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService { // Renamed from JwtUtil

    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // --- Generation Method (Already Implemented) ---
    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email) 
                .claim("userId", userId) 
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }
    
    // --- Validation and Extraction Methods (NEW) ---

    /** Parses and validates the token, returning all claims */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** Extracts a single claim (e.g., subject/email or userId) */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Extracts the user identifier (subject/email) */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Checks if the token is expired */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /** Full validation check */
    public boolean isTokenValid(String token) {
        // In a real scenario, you'd check if the username (subject) exists in the database
        // and if the token is not blacklisted.
        return !isTokenExpired(token);
    }
}