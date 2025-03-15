package com.example.inventory_backend.repository;

import com.example.inventory_backend.model.Notification;
import com.example.inventory_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByTimestampDesc(User user);
    List<Notification> findByUserAndReadOrderByTimestampDesc(User user, boolean read);
    int countByUserAndRead(User user, boolean read);
}