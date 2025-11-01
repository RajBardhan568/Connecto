package com.connecto.posts.repository;

import com.connecto.posts.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    // US 09: Find all posts created by a list of user IDs (Friend IDs + Own ID)
    // Order by creation date descending (newest first)
    List<PostEntity> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds);

    // US 03: Find posts by a single user (for their profile page)
    List<PostEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}