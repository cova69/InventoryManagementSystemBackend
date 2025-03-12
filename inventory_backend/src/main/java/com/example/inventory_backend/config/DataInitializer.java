package com.example.inventory_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.inventory_backend.model.User;
import com.example.inventory_backend.model.User.Role;
import com.example.inventory_backend.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if admin user exists, if not create it
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin user created");
        }
        
        // Create additional sample users if needed
        if (userRepository.findByEmail("manager@example.com").isEmpty()) {
            User manager = new User();
            manager.setName("Manager");
            manager.setEmail("manager@example.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setRole(Role.MANAGER);
            userRepository.save(manager);
            System.out.println("Default manager user created");
        }
        
        if (userRepository.findByEmail("employee@example.com").isEmpty()) {
            User employee = new User();
            employee.setName("Employee");
            employee.setEmail("employee@example.com");
            employee.setPassword(passwordEncoder.encode("employee123"));
            employee.setRole(Role.EMPLOYEE);
            userRepository.save(employee);
            System.out.println("Default employee user created");
        }
    }
}