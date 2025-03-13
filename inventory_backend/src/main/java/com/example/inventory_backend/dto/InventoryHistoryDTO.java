package com.example.inventory_backend.dto;

import java.time.LocalDateTime;

public class InventoryHistoryDTO {
    private Long id;
    private LocalDateTime timestamp;
    private Integer totalQuantity;
    private String formattedTimestamp;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Integer getTotalQuantity() {
        return totalQuantity;
    }
    
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    
    public String getFormattedTimestamp() {
        return formattedTimestamp;
    }
    
    public void setFormattedTimestamp(String formattedTimestamp) {
        this.formattedTimestamp = formattedTimestamp;
    }
}