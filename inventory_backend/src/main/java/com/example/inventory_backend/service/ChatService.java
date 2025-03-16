package com.example.inventory_backend.service;

import com.example.inventory_backend.dto.ChatDTO;
import com.example.inventory_backend.dto.ChatMessageDTO;
import com.example.inventory_backend.model.User;

import java.util.List;

public interface ChatService {
    List<ChatDTO> getAllUserChats(User currentUser);
    
    List<ChatDTO> getRecentChats(User currentUser);
    
    ChatDTO getChatById(Long chatId, User currentUser);
    
    List<ChatMessageDTO> getChatMessages(Long chatId, User currentUser);
    
    ChatMessageDTO sendMessage(Long chatId, String content, User sender);
    
    void markChatAsRead(Long chatId, User currentUser);
    
    int countUnreadMessages(User currentUser);
    
    ChatDTO createNewChat(User currentUser, Long otherUserId);
}