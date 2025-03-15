package com.example.inventory_backend.controller;

import com.example.inventory_backend.dto.NotificationDTO;
import com.example.inventory_backend.security.UserDetailsImpl;
import com.example.inventory_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public List<NotificationDTO> getUserNotifications() {
        Long userId = getCurrentUserId();
        return notificationService.getUserNotifications(userId);
    }
    
    @GetMapping("/unread")
    public List<NotificationDTO> getUnreadNotifications() {
        Long userId = getCurrentUserId();
        return notificationService.getUnreadNotifications(userId);
    }
    
    @GetMapping("/count-unread")
    public ResponseEntity<Map<String, Integer>> getUnreadCount() {
        Long userId = getCurrentUserId();
        int count = notificationService.countUnreadNotifications(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/mark-all-read")
    public ResponseEntity<?> markAllAsRead() {
        Long userId = getCurrentUserId();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}