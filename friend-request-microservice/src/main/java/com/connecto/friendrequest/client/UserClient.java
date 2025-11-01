package com.connecto.friendrequest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// FeignClient uses the service name registered in Consul (user-microservice)
@FeignClient(name = "user-microservice") 
public interface UserClient {

    /**
     * Endpoint to check if a user exists and retrieve their basic profile for display.
     * We need the User Microservice to expose this endpoint.
     */
    @GetMapping("/api/users/{userId}/basic") 
    // This method needs a return type matching the User Microservice's response DTO, 
    // which we'll define next.
    String checkUserExists(@PathVariable("userId") Long userId); 
}