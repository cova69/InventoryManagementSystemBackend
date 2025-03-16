package com.example.inventory_backend.dto;

import java.time.LocalDateTime;

public class ChatDTO {
    private Long id;
    private UserDTO otherParticipant;
    private ChatMessageDTO lastMessage;
    private boolean hasUnread;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserDTO getOtherParticipant() {
        return otherParticipant;
    }
    
    public void setOtherParticipant(UserDTO otherParticipant) {
        this.otherParticipant = otherParticipant;
    }
    
    public ChatMessageDTO getLastMessage() {
        return lastMessage;
    }
    
    public void setLastMessage(ChatMessageDTO lastMessage) {
        this.lastMessage = lastMessage;
    }
    
    public boolean isHasUnread() {
        return hasUnread;
    }
    
    public void setHasUnread(boolean hasUnread) {
        this.hasUnread = hasUnread;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}