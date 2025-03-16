package com.example.inventory_backend.dto;

public class ChatbotMessageResponse {
    private String message;
    private String botId = "inventory-assistant";

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }
}