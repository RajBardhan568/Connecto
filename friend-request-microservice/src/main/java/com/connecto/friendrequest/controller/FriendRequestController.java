package com.connecto.friendrequest.controller;

import com.connecto.friendrequest.entity.FriendRequestEntity;
import com.connecto.friendrequest.service.FriendRequestService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends") // Path routed by API Gateway
public class FriendRequestController {

    private final FriendRequestService service;
    
    // NOTE: In a real app, the authenticatedUserId would be extracted from the JWT.
    // For now, we assume the JWT contains the ID and the client is submitting the ID.
    // This is simplified and will be fixed with a dedicated JWT filter for this MS later.
    private Long MOCK_USER_ID = 1L; 

    public FriendRequestController(FriendRequestService service) {
        this.service = service;
    }
    
    /**
     * US 08: POST /api/friends/send/{receiverId} - Sends a request
     */
    @PostMapping("/send/{receiverId}")
    public ResponseEntity<?> sendRequest(@PathVariable Long receiverId) {
        try {
            // Using MOCK_USER_ID as the sender for now
            FriendRequestEntity request = service.sendRequest(MOCK_USER_ID, receiverId);
            return new ResponseEntity<>(request, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    
    /**
     * US 05: PUT /api/friends/accept/{requestId} - Accepts a request
     */
    @PutMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptRequest(@PathVariable Long requestId) {
        try {
            // Using MOCK_USER_ID as the receiver for the authorization check
            FriendRequestEntity connection = service.acceptRequest(requestId, MOCK_USER_ID);
            return ResponseEntity.ok(connection);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    

    /**
     * US 05: GET /api/friends/requests - View Pending Requests
     */
    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestEntity>> getPendingRequests() {
        // Using MOCK_USER_ID as the receiver
        List<FriendRequestEntity> requests = service.getPendingRequests(MOCK_USER_ID);
        return ResponseEntity.ok(requests);
    }
   
    /**
     * US 08: DELETE /api/friends/unfriend/{friendId} - Unfriend
     */
    @DeleteMapping("/unfriend/{friendId}")
    public ResponseEntity<String> unfriend(@PathVariable Long friendId) {
        try {
            // Using MOCK_USER_ID as the current user
            service.unfriend(MOCK_USER_ID, friendId);
            return ResponseEntity.ok("Successfully unfriended user ID: " + friendId);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    /**
     * GET /api/friends/{userId}/connections/ids - Used by Posts Microservice Feign client
     */
    @GetMapping("/{userId}/connections/ids")
    public ResponseEntity<List<Long>> getFriendIds(@PathVariable Long userId) {
        List<Long> friendIds = service.getAcceptedFriendIds(userId);
        return ResponseEntity.ok(friendIds);
    }
}