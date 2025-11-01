package com.connecto.friendrequest.service;

import com.connecto.friendrequest.client.UserClient;
import com.connecto.friendrequest.entity.FriendRequestEntity;
import com.connecto.friendrequest.entity.RequestStatus;
import com.connecto.friendrequest.repository.FriendRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {
    
    private final FriendRequestRepository repository;
    private final UserClient userClient;

    public FriendRequestService(FriendRequestRepository repository, UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }
    
    /**
     * US 08: Sends a friend request.
     */
    public FriendRequestEntity sendRequest(Long senderId, Long receiverId) {
        
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send a request to yourself.");
        }
        
        // 1. Check if receiver exists using Feign Client
        try {
            // This verifies the user ID exists in the User Microservice
            userClient.checkUserExists(receiverId); 
        } catch (Exception e) {
            throw new RuntimeException("Receiver user does not exist.");
        }

        // 2. Check for existing request (in either direction)
        if (repository.findBySenderIdAndReceiverId(senderId, receiverId).isPresent() ||
            repository.findBySenderIdAndReceiverId(receiverId, senderId).isPresent()) {
            throw new IllegalStateException("A friend relationship or pending request already exists.");
        }
        
        // 3. Create and save the request
        FriendRequestEntity request = new FriendRequestEntity();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setStatus(RequestStatus.PENDING);
        
        return repository.save(request);
    }
    
    /**
     * US 05: Accepts a pending friend request.
     */
    public FriendRequestEntity acceptRequest(Long requestId, Long receiverId) {
        FriendRequestEntity request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found."));
        
        // Security check: Only the receiver can accept the request
        if (!request.getReceiverId().equals(receiverId)) {
            throw new SecurityException("User not authorized to accept this request.");
        }
        
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Request is not pending.");
        }

        // 1. Update status and date
        request.setStatus(RequestStatus.ACCEPTED);
        request.setResponseDate(LocalDateTime.now());
        
        // 2. Save the connection
        // NOTE: In a complex system, you might create a separate 'Friendship' table here.
        // For now, an ACCEPTED request status serves as the active friendship record.
        return repository.save(request);
    }
    

    /**
     * US 05: Retrieves all pending friend requests for a given user (receiver).
     */
    public List<FriendRequestEntity> getPendingRequests(Long receiverId) {
        return repository.findByReceiverIdAndStatus(receiverId, RequestStatus.PENDING);
    }


    /**
     * US 08: Deletes an ACCEPTED friendship record (Unfriend).
     */
    public void unfriend(Long userId1, Long userId2) {
        // Find the friendship record (regardless of who was sender/receiver)
        Optional<FriendRequestEntity> friendship = repository.findBySenderIdAndReceiverId(userId1, userId2)
                .or(() -> repository.findBySenderIdAndReceiverId(userId2, userId1));
        
        if (friendship.isPresent() && friendship.get().getStatus() == RequestStatus.ACCEPTED) {
            repository.delete(friendship.get());
        } else {
            throw new RuntimeException("No active friendship found between these users.");
        }
    
    }


    /**
     * Helper method for Posts Microservice: Returns all IDs of users connected to this user.
     */
    public List<Long> getAcceptedFriendIds(Long userId) {
        return repository.findAllAcceptedFriendships(userId).stream()
                .map(request -> request.getSenderId().equals(userId) ? request.getReceiverId() : request.getSenderId())
                .toList();
    }
}