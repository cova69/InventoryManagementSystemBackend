package com.example.inventory_backend.dto;

public class CreateChatRequestDTO {
    private Long participantId;
    
    public Long getParticipantId() {
        return participantId;
    }
    
    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }
}