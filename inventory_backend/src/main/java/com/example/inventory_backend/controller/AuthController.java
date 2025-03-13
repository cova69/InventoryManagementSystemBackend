package com.example.inventory_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_backend.dto.JwtResponse;
import com.example.inventory_backend.dto.LoginRequest;
import com.example.inventory_backend.dto.MessageResponse;
import com.example.inventory_backend.dto.RegisterRequest;
import com.example.inventory_backend.model.User;
import com.example.inventory_backend.model.User.Role;
import com.example.inventory_backend.repository.UserRepository;
import com.example.inventory_backend.security.JwtUtils;
import com.example.inventory_backend.security.UserDetailsImpl;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(), 
            userDetails.getEmail(),
            roles));
        }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Registration attempt for user: {}", registerRequest.getEmail());
        
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            logger.warn("Registration failed: Email already in use - {}", registerRequest.getEmail());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        String requestRole = registerRequest.getRole();
        
        // Set role based on request or default to EMPLOYEE
        if (requestRole != null && !requestRole.isEmpty()) {
            try {
                Role role = Role.valueOf(requestRole.toUpperCase());
                user.setRole(role);
                logger.info("Setting role: {}", role);
            } catch (IllegalArgumentException e) {
                // If role is invalid, default to EMPLOYEE
                user.setRole(Role.EMPLOYEE);
                logger.warn("Invalid role provided: {}. Defaulting to EMPLOYEE", requestRole);
            }
        } else {
            user.setRole(Role.EMPLOYEE);
            logger.info("No role provided. Defaulting to EMPLOYEE");
        }

        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}