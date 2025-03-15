package com.example.inventory_backend.service.impl;

import com.example.inventory_backend.dto.NotificationDTO;
import com.example.inventory_backend.model.Notification;
import com.example.inventory_backend.model.User;
import com.example.inventory_backend.repository.NotificationRepository;
import com.example.inventory_backend.repository.UserRepository;
import com.example.inventory_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return notificationRepository.findByUserOrderByTimestampDesc(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return notificationRepository.findByUserAndReadOrderByTimestampDesc(user, false).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public int countUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return notificationRepository.countByUserAndRead(user, false);
    }
    
    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        
        notification.setRead(true);
        notificationRepository.save(notification);
    }
    
    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        List<Notification> notifications = notificationRepository.findByUserAndReadOrderByTimestampDesc(user, false);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }
    
    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    @Override
    @Transactional
    public void notifyNewProduct(Long productId, String productName) {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            Notification notification = new Notification();
            notification.setTitle("New Product Added");
            notification.setMessage("Product \"" + productName + "\" has been added to inventory");
            notification.setType("info");
            notification.setTimestamp(LocalDateTime.now());
            notification.setEntityType("product");
            notification.setEntityId(productId);
            notification.setUser(user);
            notification.setRead(false);
            
            // Set styling details
            notification.setIconName("ShoppingCart");
            notification.setIconColor("#3498db");
            notification.setAvatarColor("rgba(52, 152, 219, 0.2)");
            notification.setBgColor("rgba(52, 152, 219, 0.1)");
            notification.setBorderColor("rgba(52, 152, 219, 0.2)");
            
            notificationRepository.save(notification);
        }
    }
    
    @Override
    @Transactional
    public void notifyNewCategory(Long categoryId, String categoryName) {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            Notification notification = new Notification();
            notification.setTitle("New Category Added");
            notification.setMessage("Category \"" + categoryName + "\" has been created");
            notification.setType("info");
            notification.setTimestamp(LocalDateTime.now());
            notification.setEntityType("category");
            notification.setEntityId(categoryId);
            notification.setUser(user);
            notification.setRead(false);
            
            // Set styling details
            notification.setIconName("Category");
            notification.setIconColor("#2ecc71");
            notification.setAvatarColor("rgba(46, 204, 113, 0.2)");
            notification.setBgColor("rgba(46, 204, 113, 0.1)");
            notification.setBorderColor("rgba(46, 204, 113, 0.2)");
            
            notificationRepository.save(notification);
        }
    }
    
    @Override
    @Transactional
    public void notifyNewSupplier(Long supplierId, String supplierName) {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            Notification notification = new Notification();
            notification.setTitle("New Supplier Added");
            notification.setMessage("Supplier \"" + supplierName + "\" has been added");
            notification.setType("info");
            notification.setTimestamp(LocalDateTime.now());
            notification.setEntityType("supplier");
            notification.setEntityId(supplierId);
            notification.setUser(user);
            notification.setRead(false);
            
            // Set styling details
            notification.setIconName("LocalShipping");
            notification.setIconColor("#9b59b6");
            notification.setAvatarColor("rgba(155, 89, 182, 0.2)");
            notification.setBgColor("rgba(155, 89, 182, 0.1)");
            notification.setBorderColor("rgba(155, 89, 182, 0.2)");
            
            notificationRepository.save(notification);
        }
    }
    
    @Override
    @Transactional
    public void notifyNewUser(Long userId, String userName, String role) {
        // Only notify admins about new users
        List<User> admins = userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.ADMIN)
                .collect(Collectors.toList());
        
        for (User admin : admins) {
            if (admin.getId().equals(userId)) {
                // Don't notify admins about their own creation
                continue;
            }
            
            Notification notification = new Notification();
            notification.setTitle("New User Account");
            notification.setMessage("User \"" + userName + "\" has been created with " + role + " role");
            notification.setType("info");
            notification.setTimestamp(LocalDateTime.now());
            notification.setEntityType("user");
            notification.setEntityId(userId);
            notification.setUser(admin);
            notification.setRead(false);
            
            // Set styling details
            notification.setIconName("Person");
            notification.setIconColor("#e74c3c");
            notification.setAvatarColor("rgba(231, 76, 60, 0.2)");
            notification.setBgColor("rgba(231, 76, 60, 0.1)");
            notification.setBorderColor("rgba(231, 76, 60, 0.2)");
            
            notificationRepository.save(notification);
        }
    }
    
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setTimestamp(notification.getTimestamp());
        dto.setEntityType(notification.getEntityType());
        dto.setEntityId(notification.getEntityId());
        dto.setIconName(notification.getIconName());
        dto.setIconColor(notification.getIconColor());
        dto.setAvatarColor(notification.getAvatarColor());
        dto.setBgColor(notification.getBgColor());
        dto.setBorderColor(notification.getBorderColor());
        dto.setRead(notification.isRead());
        return dto;
    }
}