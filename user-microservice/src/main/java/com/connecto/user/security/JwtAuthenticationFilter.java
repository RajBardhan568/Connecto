package com.connecto.user.security;

import com.connecto.user.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User; // Using simple User object for demonstration
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Check for Authorization header and format
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract JWT
        jwt = authHeader.substring(7);
        
        try {
            // 3. Extract username (email) from token
            userEmail = jwtService.extractUsername(jwt);

            // 4. Validate Token and Authentication Context
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Mock UserDetails (In a real app, this would come from a database using UserDetailsService)
                UserDetails userDetails = new User(userEmail, "", new ArrayList<>());

                if (jwtService.isTokenValid(jwt)) {
                    // Create an authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Credentials are null for token-based auth
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // Update Security Context (User is now authenticated for this request)
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log exception (e.g., token expired, invalid signature)
            System.err.println("JWT Validation Failed: " + e.getMessage());
            // Optionally clear the authentication context or send UNAUTHORIZED status
        }

        // 5. Continue the filter chain
        filterChain.doFilter(request, response);
    }
}