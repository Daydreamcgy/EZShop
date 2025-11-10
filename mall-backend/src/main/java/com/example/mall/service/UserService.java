package com.example.mall.service;

import com.example.mall.model.User;

public interface UserService {
    User registerUser(String username, String password, String email);
    String authenticateUser(String username, String password);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User findByUsername(String username);
    User updateUser(Long userId, String email, String password);
    User findById(Long id);
}