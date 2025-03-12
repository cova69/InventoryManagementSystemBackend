package com.example.inventory_backend.service;

import com.example.inventory_backend.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    
    User getUserById(Long id);
    
    User getUserByEmail(String email);
    
    User createUser(User user);
    
    User updateUser(User user);
    
    void deleteUser(Long id);
}