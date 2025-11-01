package com.connecto.posts.controller;

import com.connecto.posts.dto.PostCreationDTO;
import com.connecto.posts.dto.PostResponseDTO;
import com.connecto.posts.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts") 
public class PostController {

    private final PostService service;
    // MOCK_USER_ID is used until we implement JWT principal extraction in this MS.
    private static final Long MOCK_USER_ID = 1L; 

    public PostController(PostService service) {
        this.service = service;
    }
    
    /**
     * US 03: POST /api/posts - Create a new post
     */
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostCreationDTO creationDTO) {
        PostResponseDTO post = service.createPost(MOCK_USER_ID, creationDTO);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    /**
     * US 09: GET /api/posts/feed - Retrieve the main news feed
     */
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDTO>> getNewsFeed() {
        List<PostResponseDTO> feed = service.getNewsFeed(MOCK_USER_ID);
        return ResponseEntity.ok(feed);
    }
    
    /**
     * US 09: PUT /api/posts/{postId}/like - Toggle like count
     */
    @PutMapping("/{postId}/like")
    public ResponseEntity<PostResponseDTO> likePost(@PathVariable Long postId) {
        try {
            PostResponseDTO updatedPost = service.toggleLike(postId);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}