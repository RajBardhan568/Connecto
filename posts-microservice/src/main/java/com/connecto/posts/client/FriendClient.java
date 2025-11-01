package com.connecto.posts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "friend-request-microservice") 
public interface FriendClient {

    /**
     * Retrieves a list of all accepted friend IDs for a given user.
     * NOTE: We must implement this endpoint in the Friend Request MS next.
     */
    @GetMapping("/api/friends/{userId}/connections/ids") 
    List<Long> getFriendIds(@PathVariable("userId") Long userId);
}