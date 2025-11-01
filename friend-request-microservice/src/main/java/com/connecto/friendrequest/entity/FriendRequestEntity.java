package com.connecto.friendrequest.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "friend_requests", uniqueConstraints = {
    // Constraint to prevent duplicate requests (e.g., A -> B and B -> A simultaneously)
    @UniqueConstraint(columnNames = {"senderId", "receiverId"}) 
})
@Data

public class FriendRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who initiated the request
    private Long senderId;
    
    // The user who received the request
    private Long receiverId;
    
    // Status can be PENDING, ACCEPTED, or DECLINED
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING; 
    
    private LocalDateTime sentDate = LocalDateTime.now();
    private LocalDateTime responseDate;
}

