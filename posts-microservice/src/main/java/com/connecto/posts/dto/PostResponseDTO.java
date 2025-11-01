package com.connecto.posts.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class PostResponseDTO {
    private Long id;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private int likesCount;
    // Embed the user data for the frontend display
    private UserBasicDTO author; 
    
}