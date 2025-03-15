package com.example.inventory_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String message;
    private String type;  // "info", "warning", "success", etc.
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    // Track which entity this notification is about
    private String entityType;  // "product", "category", "supplier", "user"
    private Long entityId;
    
    // Icon info for frontend
    private String iconName;  // Material icon name
    private String iconColor;  // Color for the icon
    private String avatarColor;  // Background color for avatar
    private String bgColor;  // Background color for notification card
    private String borderColor;  // Border color for notification card
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Who this notification is for
    
    private boolean read;
}