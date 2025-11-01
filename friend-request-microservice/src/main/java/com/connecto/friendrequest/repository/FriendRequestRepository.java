package com.connecto.friendrequest.repository;

import com.connecto.friendrequest.entity.FriendRequestEntity;
import com.connecto.friendrequest.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // 👈 NEW IMPORT

import java.util.List;
import java.util.Optional;

// ... interface definition ...
public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {

    // Check for an existing request between two users (in either direction)
    Optional<FriendRequestEntity> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    // US 05: Find all PENDING requests sent TO this user (Friend Requests Tab)
    List<FriendRequestEntity> findByReceiverIdAndStatus(Long receiverId, RequestStatus status);


    // Find all ACCEPTED relationships where the user is either the sender or the receiver
    // USING NAMED PARAMETERS (@Param("userId")) is much safer than positional (?1)
    @Query("SELECT r FROM FriendRequestEntity r " +
           "WHERE (r.senderId = :userId OR r.receiverId = :userId) " +
           "AND r.status = 'ACCEPTED'")
    List<FriendRequestEntity> findAllAcceptedFriendships(@Param("userId") Long userId); // 👈 USE @Param
}
