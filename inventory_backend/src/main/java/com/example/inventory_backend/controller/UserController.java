package com.example.inventory_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_backend.dto.MessageResponse;
import com.example.inventory_backend.dto.UserDTO;
import com.example.inventory_backend.model.User;
import com.example.inventory_backend.model.User.Role;
import com.example.inventory_backend.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")  
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("User controller is accessible");
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(convertToDTO(user));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Password is required"));
        }
        
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(convertToDTO(createdUser), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            User existingUser = userService.getUserById(id);
            
            existingUser.setName(userDTO.getName());
            existingUser.setEmail(userDTO.getEmail());
            
            // Only update password if provided
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            
            if (userDTO.getRole() != null) {
                try {
                    Role role = Role.valueOf(userDTO.getRole());
                    existingUser.setRole(role);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Error: Invalid role specified"));
                }
            }
            
            User updatedUser = userService.updateUser(existingUser);
            return ResponseEntity.ok(convertToDTO(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
    
    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        
        // Password will be set in the controller
        
        if (dto.getRole() != null) {
            try {
                Role role = Role.valueOf(dto.getRole());
                user.setRole(role);
            } catch (IllegalArgumentException e) {
                // Default to EMPLOYEE if invalid role
                user.setRole(Role.EMPLOYEE);
            }
        } else {
            user.setRole(Role.EMPLOYEE);
        }
        
        return user;
    }
}