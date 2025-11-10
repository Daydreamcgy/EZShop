package com.example.mall.controller;

import com.example.mall.model.User;
import com.example.mall.payload.JwtResponse;
import com.example.mall.payload.LoginRequest;
import com.example.mall.payload.RegisterRequest;
import com.example.mall.security.JwtUtils;
import com.example.mall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        logger.info("Attempting to register user: {}", registerRequest.getUsername());
        
        if (userService.existsByUsername(registerRequest.getUsername())) {
            logger.warn("Registration failed: Username {} is already taken", registerRequest.getUsername());
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            logger.warn("Registration failed: Email {} is already in use", registerRequest.getEmail());
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        User user = userService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail()
        );
        
        logger.info("User registered successfully: {}", user.getUsername());

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Attempting to authenticate user: {}", loginRequest.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            logger.info("User authenticated successfully: {}", loginRequest.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.badRequest().body("Invalid username or password!");
        }
    }
}