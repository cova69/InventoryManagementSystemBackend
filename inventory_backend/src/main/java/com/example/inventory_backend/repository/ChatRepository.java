package com.example.inventory_backend.repository;

import com.example.inventory_backend.model.Chat;
import com.example.inventory_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p = :user ORDER BY c.updatedAt DESC")
    List<Chat> findByParticipant(@Param("user") User user);
    
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p = :user ORDER BY c.updatedAt DESC LIMIT 10")
    List<Chat> findRecentByParticipant(@Param("user") User user);
    
    @Query("SELECT c FROM Chat c WHERE :user1 MEMBER OF c.participants AND :user2 MEMBER OF c.participants")
    List<Chat> findByTwoParticipants(@Param("user1") User user1, @Param("user2") User user2);
}
