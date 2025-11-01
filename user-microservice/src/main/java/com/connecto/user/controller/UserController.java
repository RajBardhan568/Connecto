package com.connecto.user.controller;

import com.connecto.user.dto.UserProfileDTO; // NEW IMPORT
import com.connecto.user.dto.UserUpdateDTO; // NEW IMPORT
import com.connecto.user.dto.LoginDTO; 
import com.connecto.user.dto.LoginResponseDTO; 
import com.connecto.user.dto.UserRegistrationDTO;
import com.connecto.user.entity.UserEntity;
import com.connecto.user.service.UserService;
import com.connecto.user.util.JwtUtil; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // NEW IMPORT
import org.springframework.web.bind.annotation.*;
import com.connecto.user.dto.UserBasicDTO; // NEW IMPORT
import java.util.Optional;


    
    @RestController
    @RequestMapping("/api/users") // Matches the path used in Angular's AuthService
    public class UserController {
        
        private final UserService userService;
        private final JwtUtil jwtUtil; // INJECT JWT UTILITY
        
    public UserController(UserService userService, JwtUtil jwtUtil) {
        
            this.userService = userService;
            this.jwtUtil = jwtUtil;
    }

    
    // Handles POST request from Angular /api/users/register (US 01)
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200") // 👈 CRITICAL: Allows Angular to call this endpoint
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            UserEntity registeredUser = userService.registerNewUser(registrationDTO);
            return new ResponseEntity<>("User registered successfully with ID: " + registeredUser.getId(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // NOTE: We will replace this with the GlobalExceptionHandler later
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); 
        }
    }



    /**
     * US 02: Handles the Login API call from the Angular frontend.
     */
    @PostMapping("/login")
    // Note: CORS is handled globally by SecurityConfig, but adding it here doesn't hurt.
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        
        // 1. Authenticate user
        Optional<UserEntity> user = userService.authenticateUser(loginDTO);
        
        if (user.isPresent()) {
            // Success: In a later step, we will return a JWT here instead of the User entity.
            // For now, returning the user info signals success.
            return ResponseEntity.ok(user.get()); 
        } else {
            // Failure: User not found or password mismatch
            return new ResponseEntity<>("Invalid email or password.", HttpStatus.UNAUTHORIZED);
        }
    }




// Helper method to get the authenticated user's ID from the JWT principal
    private Long getAuthenticatedUserId(Authentication authentication) {
        // --- BEGIN TEMPORARY SIMPLIFICATION ---
        // Let's use the email and search for the user ID for robustness:
        String email = authentication.getName();
        // **This requires adding findByEmail to UserRepository, which we already have**
        return userService.getUserIdByEmail(email); 
    }
    
    /**
     * US 04: GET /api/users/{userId}/profile - View Profile (SECURED)
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> viewProfile(@PathVariable Long userId) {
        try {
            UserProfileDTO profile = userService.getProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * US 04: PUT /api/users/{userId}/profile - Update Profile (SECURED)
     */
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @PathVariable Long userId,
            @RequestBody UserUpdateDTO updateDTO) {
        try {
            UserProfileDTO updatedProfile = userService.updateProfile(userId, updateDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * GET /api/users/{userId}/basic - Basic Profile View (FOR INTER-SERVICE CALLS)
     */
    @GetMapping("/{userId}/basic")
    public ResponseEntity<UserBasicDTO> getBasicProfile(@PathVariable Long userId) {
        try {
            UserBasicDTO profile = userService.getBasicProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            // Return 404 NOT FOUND if user doesn't exist, which is critical for the Feign client check
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }

    

}







