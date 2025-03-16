package com.example.inventory_backend.controller;

import com.example.inventory_backend.dto.*;
import com.example.inventory_backend.model.User;
import com.example.inventory_backend.security.UserDetailsImpl;
import com.example.inventory_backend.service.ChatService;
import com.example.inventory_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class ChatController {

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<ChatDTO>> getAllChats() {
        User currentUser = getCurrentUser();
        List<ChatDTO> chats = chatService.getAllUserChats(currentUser);
        return ResponseEntity.ok(chats);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<ChatDTO>> getRecentChats() {
        User currentUser = getCurrentUser();
        List<ChatDTO> recentChats = chatService.getRecentChats(currentUser);
        return ResponseEntity.ok(recentChats);
    }
    
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Integer>> getUnreadCount() {
        User currentUser = getCurrentUser();
        int unreadCount = chatService.countUnreadMessages(currentUser);
        return ResponseEntity.ok(Map.of("count", unreadCount));
    }
    
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable Long chatId) {
        User currentUser = getCurrentUser();
        ChatDTO chat = chatService.getChatById(chatId, currentUser);
        return ResponseEntity.ok(chat);
    }
    
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Long chatId) {
        User currentUser = getCurrentUser();
        List<ChatMessageDTO> messages = chatService.getChatMessages(chatId, currentUser);
        return ResponseEntity.ok(messages);
    }
    
    @PostMapping("/{chatId}/messages")
    public ResponseEntity<ChatMessageDTO> sendMessage(
            @PathVariable Long chatId,
            @RequestBody SendMessageRequestDTO request) {
        User currentUser = getCurrentUser();
        ChatMessageDTO message = chatService.sendMessage(chatId, request.getContent(), currentUser);
        return ResponseEntity.ok(message);
    }
    
    @PutMapping("/{chatId}/read")
    public ResponseEntity<?> markChatAsRead(@PathVariable Long chatId) {
        User currentUser = getCurrentUser();
        chatService.markChatAsRead(chatId, currentUser);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping
    public ResponseEntity<ChatDTO> createChat(@RequestBody CreateChatRequestDTO request) {
        User currentUser = getCurrentUser();
        ChatDTO chat = chatService.createNewChat(currentUser, request.getParticipantId());
        return ResponseEntity.ok(chat);
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userService.getUserById(userDetails.getId());
    }
}