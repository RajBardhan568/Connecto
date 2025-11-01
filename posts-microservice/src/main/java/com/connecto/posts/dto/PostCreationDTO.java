package com.connecto.posts.dto;

import lombok.Data;

@Data
public class PostCreationDTO {
    private String content;
    private String imageUrl; // Optional
}