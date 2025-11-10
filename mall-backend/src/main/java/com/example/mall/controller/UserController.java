package com.example.mall.controller;

import com.example.mall.model.User;
import com.example.mall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        logger.info("Getting current user information");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            
            User user = userService.findByUsername(username);
            if (user != null) {
                // 清除敏感信息
                user.setPassword(null);
                logger.info("Current user information retrieved successfully for user: {}", username);
                return ResponseEntity.ok(user);
            }
        }
        
        logger.warn("Failed to get current user information");
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(@RequestBody UpdateUserRequest updateUserRequest) {
        logger.info("Updating current user information");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            
            User user = userService.findByUsername(username);
            if (user != null) {
                User updatedUser = userService.updateUser(
                    user.getId(), 
                    updateUserRequest.getEmail(), 
                    updateUserRequest.getPassword()
                );
                
                if (updatedUser != null) {
                    // 清除敏感信息
                    updatedUser.setPassword(null);
                    logger.info("Current user information updated successfully for user: {}", username);
                    return ResponseEntity.ok(updatedUser);
                }
            }
        }
        
        logger.warn("Failed to update current user information");
        return ResponseEntity.notFound().build();
    }
    
    // 更新用户信息的请求体类
    public static class UpdateUserRequest {
        private String email;
        private String password;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}