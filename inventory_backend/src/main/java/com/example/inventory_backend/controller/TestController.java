package com.example.inventory_backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    @GetMapping("/public")
    public Map<String, String> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Public content accessible without authentication");
        return response;
    }
    
    @GetMapping("/user")
    public Map<String, String> userEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User content accessible by authenticated users");
        return response;
    }
    
    @GetMapping("/manager")
    public Map<String, String> managerEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Manager content accessible by users with MANAGER role");
        return response;
    }
    
    @GetMapping("/admin")
    public Map<String, String> adminEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin content accessible by users with ADMIN role");
        return response;
    }
}