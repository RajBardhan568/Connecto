package com.connecto.user.repository;

import com.connecto.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Custom query method for US 01 uniqueness check
    Optional<UserEntity> findByEmailAddress(String emailAddress);
}