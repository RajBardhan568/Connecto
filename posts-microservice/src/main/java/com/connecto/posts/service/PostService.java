package com.connecto.posts.service;

import com.connecto.posts.client.FriendClient;
import com.connecto.posts.client.UserClient;
import com.connecto.posts.dto.PostCreationDTO;
import com.connecto.posts.dto.PostResponseDTO;
import com.connecto.posts.entity.PostEntity;
import com.connecto.posts.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.connecto.posts.dto.UserBasicDTO; // Assuming UserBasicDTO is now in the DTO package

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;
    private final FriendClient friendClient;
    private final UserClient userClient;
    private final ModelMapper modelMapper;

    public PostService(PostRepository repository, FriendClient friendClient, UserClient userClient, ModelMapper modelMapper) {
        this.repository = repository;
        this.friendClient = friendClient;
        this.userClient = userClient;
        // The injected ModelMapper should be used, not creating a new one.
        this.modelMapper = modelMapper; 
        // FIX: REMOVE the redundant initialization: this.modelMapper = new ModelMapper(); 
    }

    /**
     * US 03: Creates a new post.
     */
    public PostResponseDTO createPost(Long userId, PostCreationDTO creationDTO) {
        PostEntity post = modelMapper.map(creationDTO, PostEntity.class);
        post.setUserId(userId);
        PostEntity savedPost = repository.save(post);
        
        // FIX 1: Referenced savedPost
        UserBasicDTO author = userClient.getBasicProfile(savedPost.getUserId());
        PostResponseDTO response = modelMapper.map(savedPost, PostResponseDTO.class);
        response.setAuthor(author);
        return response;
    }

    /**
     * US 09: Retrieves the main news feed (posts from friends + self).
     */
    public List<PostResponseDTO> getNewsFeed(Long currentUserId) {
        // 1. Get friend IDs (Inter-service call)
        List<Long> friendIds = friendClient.getFriendIds(currentUserId);
        
        // 2. Add current user's ID to the list
        List<Long> userIds = new ArrayList<>(friendIds);
        userIds.add(currentUserId);
        
        // 3. Fetch posts from the database (repository query)
        List<PostEntity> postEntities = repository.findByUserIdInOrderByCreatedAtDesc(userIds);
        
        // 4. Convert and enhance with author data
        return postEntities.stream()
                .map(postEntity -> {
                    // Fetch author data via Feign (can be optimized with bulk call)
                    UserBasicDTO author = userClient.getBasicProfile(postEntity.getUserId());
                    PostResponseDTO response = modelMapper.map(postEntity, PostResponseDTO.class);
                    response.setAuthor(author);
                    return response;
                })
                .toList();
    }
    
    /**
     * US 09: Toggles a like on a post. (Simplified: just increments count for now)
     */
    public PostResponseDTO toggleLike(Long postId) {
        PostEntity post = repository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found."));
        
        // Simple toggle logic (a separate 'Likes' table would be required for true tracking)
        post.setLikesCount(post.getLikesCount() + 1); 
        
        PostEntity updatedPost = repository.save(post);
        
        // FIX 2: Referenced updatedPost
        UserBasicDTO author = userClient.getBasicProfile(updatedPost.getUserId());
        PostResponseDTO response = modelMapper.map(updatedPost, PostResponseDTO.class);
        response.setAuthor(author);
        return response;
    }
}