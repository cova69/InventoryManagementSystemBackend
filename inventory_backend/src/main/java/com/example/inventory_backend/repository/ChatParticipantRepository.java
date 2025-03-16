package com.example.inventory_backend.repository;

import com.example.inventory_backend.model.Chat;
import com.example.inventory_backend.model.ChatParticipant;
import com.example.inventory_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    
    List<ChatParticipant> findByChatAndUser(Chat chat, User user);
    
    @Query("SELECT MAX(cp.lastReadMessageId) FROM ChatParticipant cp WHERE cp.chat = :chat AND cp.user = :user")
    Long findLastReadMessageId(@Param("chat") Chat chat, @Param("user") User user);
}