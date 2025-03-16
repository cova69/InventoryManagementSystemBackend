package com.example.inventory_backend.repository;

import com.example.inventory_backend.model.Chat;
import com.example.inventory_backend.model.ChatMessage;
import com.example.inventory_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findByChatOrderByTimestampAsc(Chat chat);
    
    @Query("SELECT m FROM ChatMessage m WHERE m.chat = :chat ORDER BY m.timestamp DESC LIMIT 1")
    Optional<ChatMessage> findLatestMessageByChat(@Param("chat") Chat chat);
    
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.chat = :chat AND m.sender != :user AND m.read = false")
    int countUnreadMessagesByChat(@Param("chat") Chat chat, @Param("user") User user);
    
    @Query("SELECT COUNT(m) FROM ChatMessage m JOIN m.chat c JOIN c.participants p WHERE p = :user AND m.sender != :user AND m.read = false")
    int countTotalUnreadMessages(@Param("user") User user);
}
