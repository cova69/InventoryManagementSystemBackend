// InventoryServiceImpl.java
package com.example.inventory_backend.service.impl;

import com.example.inventory_backend.model.Inventory;
import com.example.inventory_backend.model.Product;
import com.example.inventory_backend.repository.InventoryRepository;
import com.example.inventory_backend.repository.ProductRepository;
import com.example.inventory_backend.service.InventoryHistoryService;
import com.example.inventory_backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    
    @Autowired
    private InventoryHistoryService historyService;
    
    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
    
    @Override
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
    }
    
    @Override
    public Inventory getInventoryByProduct(Product product) {
        return inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + product.getName()));
    }
    
    @Override
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }
    
    @Override
    public Inventory saveInventory(Inventory inventory) {
        boolean isNewInventory = inventory.getId() == null;
        inventory.setLastUpdated(LocalDateTime.now());
        Inventory savedInventory = inventoryRepository.save(inventory);
        
        // Record history
        if (isNewInventory) {
            historyService.recordProductChange(inventory.getProduct(), inventory.getQuantity(), "Initial inventory");
        } else {
            historyService.recordInventoryState();
        }
        
        return savedInventory;
    }
    
    @Override
    @Transactional
    public Inventory updateQuantity(Long productId, Integer quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        Inventory inventory = inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + product.getName()));
        
        inventory.setQuantity(inventory.getQuantity() + quantityChange);
        inventory.setLastUpdated(LocalDateTime.now());
        
        Inventory updatedInventory = inventoryRepository.save(inventory);
        
        // Record history after update
        historyService.recordProductChange(product, quantityChange, 
                quantityChange > 0 ? "Stock increase" : "Stock decrease");
        
        return updatedInventory;
    }
    
    @Override
    public boolean isInStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        Inventory inventory = inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + product.getName()));
        
        return inventory.getQuantity() >= quantity;
    }
    
    @Override
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        
        Product product = inventory.getProduct();
        
        inventoryRepository.deleteById(id);
        
        // Record the deletion in history
        historyService.recordProductChange(product, -inventory.getQuantity(), "Inventory deleted");
    }
}