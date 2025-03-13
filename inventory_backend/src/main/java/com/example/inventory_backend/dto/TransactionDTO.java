package com.example.inventory_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String transactionType;
    private Integer quantity;
    private LocalDateTime transactionDate;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String notes;
    private Long userId;
    private String userName;
    private String referenceNumber;
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}