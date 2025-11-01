package com.connecto.posts.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FIX 1 & 2: Added @Column annotation to explicitly map to user_id
    // and enforced the nullable constraint.
    @Column(name = "user_id", nullable = false) 
    private Long userId; 
    
    // Using @Lob is the standard JPA way for large text fields
    @Lob
    private String content; 
    
    private String imageUrl; // Optional field for image posts
    
    @Column(updatable = false) // Ensures the creation date isn't changed on update
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private int likesCount = 0;
}
