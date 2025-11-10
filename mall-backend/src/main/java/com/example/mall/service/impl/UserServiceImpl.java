package com.example.mall.service.impl;

import com.example.mall.model.User;
import com.example.mall.repository.UserRepository;
import com.example.mall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public User registerUser(String username, String password, String email) {
        logger.info("Registering new user: {}", username);
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        
        User savedUser = userRepository.save(user);
        
        logger.info("User registered successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public String authenticateUser(String username, String password) {
        logger.info("Authenticating user: {}", username);
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // In a real implementation, we would generate and return a JWT token
        // For now, we'll return a placeholder
        logger.info("User authenticated successfully: {}", username);
        return "jwt-token";
    }

    @Override
    public Boolean existsByUsername(String username) {
        boolean exists = userRepository.existsByUsername(username);
        if (exists) {
            logger.debug("Username {} already exists", username);
        }
        return exists;
    }

    @Override
    public Boolean existsByEmail(String email) {
        boolean exists = userRepository.existsByEmail(email);
        if (exists) {
            logger.debug("Email {} already exists", email);
        }
        return exists;
    }
    
    @Override
    public User findByUsername(String username) {
        logger.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    public User updateUser(Long userId, String email, String password) {
        logger.info("Updating user with ID: {}", userId);
        
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (email != null && !email.isEmpty() && !email.equals(user.getEmail())) {
                // 只有当邮箱不为空且与当前邮箱不同时才更新邮箱
                if (userRepository.existsByEmail(email)) {
                    logger.warn("Email {} already exists", email);
                    throw new RuntimeException("Email already exists");
                }
                user.setEmail(email);
            }
            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            
            User updatedUser = userRepository.save(user);
            logger.info("User updated successfully with ID: {}", updatedUser.getId());
            return updatedUser;
        } else {
            logger.warn("User not found with ID: {}", userId);
            return null;
        }
    }

    @Override
    public User findById(Long id) {
        logger.debug("Finding user by ID: {}", id);
        return userRepository.findById(id).orElse(null);
    }
}