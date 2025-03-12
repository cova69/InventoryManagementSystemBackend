package com.example.inventory_backend.service;

import com.example.inventory_backend.model.Inventory;
import com.example.inventory_backend.model.Product;

import java.util.List;

public interface InventoryService {
    List<Inventory> getAllInventory();
    
    Inventory getInventoryById(Long id);
    
    Inventory getInventoryByProduct(Product product);
    
    List<Inventory> getLowStockItems();
    
    Inventory saveInventory(Inventory inventory);
    
    Inventory updateQuantity(Long productId, Integer quantityChange);
    
    boolean isInStock(Long productId, Integer quantity);
    
    void deleteInventory(Long id);
}
