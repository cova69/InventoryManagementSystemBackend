package com.example.inventory_backend.service.impl;

import com.example.inventory_backend.model.User;
import com.example.inventory_backend.repository.UserRepository;
import com.example.inventory_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    @Override
    public User createUser(User user) {
        // You might want to add password hashing here in a real application
        return userRepository.save(user);
    }
    
    @Override
    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());
        
        // Update user fields
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        
        // Don't update password if it's empty or null
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // You might want to add password hashing here in a real application
            existingUser.setPassword(user.getPassword());
        }
        
        existingUser.setRole(user.getRole());
        
        return userRepository.save(existingUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}