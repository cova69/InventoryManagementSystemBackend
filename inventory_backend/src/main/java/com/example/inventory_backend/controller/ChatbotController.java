package com.example.inventory_backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.inventory_backend.dto.ChatbotMessageRequest;
import com.example.inventory_backend.dto.ChatbotMessageResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:3000", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class ChatbotController {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotController.class);
    
    @Value("${rasa.url:http://localhost:5005}")
    private String rasaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/message")
    public ResponseEntity<ChatbotMessageResponse> sendMessage(@RequestBody ChatbotMessageRequest request) {
        try {
            logger.info("Received message: {}", request.getMessage());
            
            // Create request body for Rasa
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("sender", request.getUserId() != null ? request.getUserId() : UUID.randomUUID().toString());
            requestBody.put("message", request.getMessage());
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Create HTTP entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Send request to Rasa
            String rasaEndpoint = rasaUrl + "/webhooks/rest/webhook";
            logger.info("Sending request to Rasa: {}", rasaEndpoint);
            
            ResponseEntity<Object[]> rasaResponse = restTemplate.exchange(
                rasaEndpoint, 
                HttpMethod.POST, 
                entity, 
                Object[].class
            );
            
            logger.info("Received response from Rasa: {}", rasaResponse);
            
            // Extract text from Rasa response
            Object[] responses = rasaResponse.getBody();
            String responseText = "I'm sorry, I couldn't process your request.";
            
            if (responses != null && responses.length > 0) {
                // Extract text from first response
                @SuppressWarnings("unchecked")
                Map<String, Object> firstResponse = (Map<String, Object>) responses[0];
                if (firstResponse.containsKey("text")) {
                    responseText = firstResponse.get("text").toString();
                }
            }
            
            // Create response
            ChatbotMessageResponse response = new ChatbotMessageResponse();
            response.setMessage(responseText);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
            
            ChatbotMessageResponse errorResponse = new ChatbotMessageResponse();
            errorResponse.setMessage("Sorry, the chatbot service is currently unavailable. Please try again later.");
            
            return ResponseEntity.ok(errorResponse);
        }
    }
}