package com.connecto.posts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.connecto.posts.dto.UserBasicDTO; 
// Placeholder DTO for the response from user-microservice:


@FeignClient(name = "user-microservice") 
public interface UserClient {

    // Calls the endpoint we created in the last step
    @GetMapping("/api/users/{userId}/basic") 
    UserBasicDTO getBasicProfile(@PathVariable("userId") Long userId); 
}