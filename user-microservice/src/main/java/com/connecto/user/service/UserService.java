package com.connecto.user.service;
import com.connecto.user.dto.LoginDTO; 
import com.connecto.user.dto.UserRegistrationDTO;
import com.connecto.user.dto.UserUpdateDTO; // NEW IMPORT
import com.connecto.user.dto.UserProfileDTO; // NEW IMPORT
import com.connecto.user.entity.UserEntity;
import com.connecto.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional; 
import com.connecto.user.dto.UserBasicDTO; // 👈 NEW IMPORT


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserEntity registerNewUser(UserRegistrationDTO registrationDTO) {
        
        // 1. Uniqueness Check (US 01)
        if (userRepository.findByEmailAddress(registrationDTO.getEmailAddress()).isPresent()) {
            throw new RuntimeException("User with this email address already exists."); 
            // NOTE: We will replace this with a proper custom exception later (as discussed)
        }

        // 2. Convert DTO to Entity
        UserEntity userEntity = modelMapper.map(registrationDTO, UserEntity.class);
        
        // 3. Hash Password (Security requirement)
        String hashedPassword = passwordEncoder.encode(registrationDTO.getPassword());
        userEntity.setPasswordHash(hashedPassword); // Store the HASH

        // 4. Save to Database
        return userRepository.save(userEntity);
    }

    /**
     * US 02: Authenticates a user based on email and password.
     */
    public Optional<UserEntity> authenticateUser(LoginDTO loginDTO) {
        // 1. Find the user by email
        Optional<UserEntity> userOptional = userRepository.findByEmailAddress(loginDTO.getEmailOrPhone());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            // 2. Verify the submitted password against the stored hash
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
                return Optional.of(user); // Authentication successful
            }
        }
        
        // Return empty if user not found or password doesn't match
        return Optional.empty(); 
    }

    /**
     * US 04: Retrieves a user's profile details.
     * @param userId The ID of the authenticated user.
     */
    public UserProfileDTO getProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
        
        // Convert Entity to DTO
        return modelMapper.map(userEntity, UserProfileDTO.class);
    }
    
    /**
     * US 04: Updates an existing user's profile details.
     * @param userId The ID of the authenticated user.
     * @param updateDTO The data submitted by the user.
     */
    public UserProfileDTO updateProfile(Long userId, UserUpdateDTO updateDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for update."));

        // 1. Apply changes from DTO to Entity
        userEntity.setFirstName(updateDTO.getFirstName());
        userEntity.setSurname(updateDTO.getSurname());
        userEntity.setDateOfBirth(updateDTO.getDateOfBirth());
        userEntity.setGender(updateDTO.getGender());
        userEntity.setBio(updateDTO.getBio());

        // 2. Save and convert back to DTO
        UserEntity updatedEntity = userRepository.save(userEntity);
        return modelMapper.map(updatedEntity, UserProfileDTO.class);
    }



    /**
     * Helper method used by Feign client in Friend Request Microservice for ID verification.
     * @param userId The ID of the user to check.
     */
    public UserBasicDTO getBasicProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for basic profile check."));
        
        // Convert Entity to DTO
        return modelMapper.map(userEntity, UserBasicDTO.class);
    }

/**
 * Retrieves a user's ID using their email address.
 * Used by controllers or other microservices.
 */
public Long getUserIdByEmail(String email) {
    UserEntity user = userRepository.findByEmailAddress(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    return user.getId();
}



}





