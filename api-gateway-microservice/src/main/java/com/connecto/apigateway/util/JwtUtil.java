package com.connecto.apigateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void validateToken(final String token) {
        try {
            // ✅ Correct syntax for JJWT 0.12.5
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build();

            Claims claims = parser.parseClaimsJws(token).getBody();

            // Optional expiration validation
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                throw new RuntimeException("JWT Validation failed: Token has expired.");
            }

        } catch (JwtException e) {
            System.err.println("JWT Validation failed: " + e.getMessage());
            throw new RuntimeException("JWT Validation failed: Invalid or expired token.");
        }
    }
}
