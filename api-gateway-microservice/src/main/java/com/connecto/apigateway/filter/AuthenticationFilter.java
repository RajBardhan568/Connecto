package com.connecto.apigateway.filter;

import com.connecto.apigateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    // Define public paths that bypass authentication check
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/users/register",
            "/api/users/login"
    );

    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 1. Check if the path requires authentication
            if (!PUBLIC_PATHS.contains(path)) {
                
                // 2. Extract Authorization header (handles null/empty list safely)
                List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION); 

                if (authHeaders == null || authHeaders.isEmpty()) { 
                     // FIX: Ensures the request stops immediately if no header is present
                     return this.onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
                }
                
                // We know authHeaders has at least one element here
                String authHeader = authHeaders.get(0); 
                
                // 3. Validate Token Format and Signature
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    try {
                        // This relies on JwtUtil.java compiling correctly!
                        jwtUtil.validateToken(token); 
                    } catch (RuntimeException e) {
                        System.err.println("Gateway JWT Validation failed: " + e.getMessage());
                        return this.onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    return this.onError(exchange, "Authorization header must start with Bearer", HttpStatus.UNAUTHORIZED);
                }
            }
            
            // 4. Token is valid or path is public, proceed to routing
            return chain.filter(exchange);
        });
    }
    
    // Helper method to stop processing and return an error response
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        // FIX: Added body logging for better debug experience (optional)
        System.err.println("Security Error: " + err);
        return response.setComplete();
    }
}
