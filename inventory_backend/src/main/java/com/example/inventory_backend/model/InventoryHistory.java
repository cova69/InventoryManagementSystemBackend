package com.example.inventory_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_history")
@Data
public class InventoryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private Integer totalQuantity;
    
    // Optional fields to track individual product changes
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    private Integer quantityChange;
    
    private String changeReason;
}