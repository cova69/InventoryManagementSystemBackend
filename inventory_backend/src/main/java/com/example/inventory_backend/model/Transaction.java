// Transaction.java
package com.example.inventory_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
    
    private BigDecimal unitPrice;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    private String notes;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;
    
    @Column(name = "reference_number")
    private String referenceNumber;
    
    public enum TransactionType {
        PURCHASE, 
        SALE, 
        RETURN, 
        ADJUSTMENT, 
        TRANSFER
    }
}