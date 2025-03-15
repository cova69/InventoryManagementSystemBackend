package com.example.inventory_backend.service;

import com.example.inventory_backend.dto.NotificationDTO;
import com.example.inventory_backend.model.User;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getUserNotifications(Long userId);
    List<NotificationDTO> getUnreadNotifications(Long userId);
    int countUnreadNotifications(Long userId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    void deleteNotification(Long notificationId);
    
    // Methods for creating new notifications
    void notifyNewProduct(Long productId, String productName);
    void notifyNewCategory(Long categoryId, String categoryName);
    void notifyNewSupplier(Long supplierId, String supplierName);
    void notifyNewUser(Long userId, String userName, String role);
}