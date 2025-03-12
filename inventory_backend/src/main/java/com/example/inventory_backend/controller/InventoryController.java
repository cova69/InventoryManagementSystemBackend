// InventoryController.java
package com.example.inventory_backend.controller;

import com.example.inventory_backend.dto.InventoryDTO;
import com.example.inventory_backend.model.Inventory;
import com.example.inventory_backend.model.Product;
import com.example.inventory_backend.service.InventoryService;
import com.example.inventory_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    @Autowired
    public InventoryController(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    @GetMapping
    public List<InventoryDTO> getAllInventory() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        return inventoryList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id) {
        try {
            Inventory inventory = inventoryService.getInventoryById(id);
            return ResponseEntity.ok(convertToDTO(inventory));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryDTO> getInventoryByProduct(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            Inventory inventory = inventoryService.getInventoryByProduct(product);
            return ResponseEntity.ok(convertToDTO(inventory));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/low-stock")
    public List<InventoryDTO> getLowStockItems() {
        List<Inventory> lowStockItems = inventoryService.getLowStockItems();
        return lowStockItems.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = convertToEntity(inventoryDTO);
        inventory.setLastUpdated(LocalDateTime.now());
        Inventory savedInventory = inventoryService.saveInventory(inventory);
        return new ResponseEntity<>(convertToDTO(savedInventory), HttpStatus.CREATED);
    }
    
    @PatchMapping("/update-quantity/{productId}")
    public ResponseEntity<InventoryDTO> updateQuantity(
            @PathVariable Long productId, 
            @RequestBody Map<String, Integer> quantityUpdate) {
        
        Integer quantityChange = quantityUpdate.get("quantityChange");
        if (quantityChange == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Inventory updatedInventory = inventoryService.updateQuantity(productId, quantityChange);
            return ResponseEntity.ok(convertToDTO(updatedInventory));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/check-stock/{productId}")
    public ResponseEntity<Map<String, Boolean>> checkStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        
        boolean inStock = inventoryService.isInStock(productId, quantity);
        return ResponseEntity.ok(Map.of("inStock", inStock));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        try {
            Inventory existingInventory = inventoryService.getInventoryById(id);
            
            existingInventory.setQuantity(inventoryDTO.getQuantity());
            existingInventory.setReorderLevel(inventoryDTO.getReorderLevel());
            existingInventory.setReorderQuantity(inventoryDTO.getReorderQuantity());
            existingInventory.setLocation(inventoryDTO.getLocation());
            existingInventory.setLastUpdated(LocalDateTime.now());
            
            Inventory updatedInventory = inventoryService.saveInventory(existingInventory);
            return ResponseEntity.ok(convertToDTO(updatedInventory));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        try {
            inventoryService.deleteInventory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setQuantity(inventory.getQuantity());
        dto.setReorderLevel(inventory.getReorderLevel());
        dto.setReorderQuantity(inventory.getReorderQuantity());
        dto.setLocation(inventory.getLocation());
        dto.setLastUpdated(inventory.getLastUpdated());
        
        if (inventory.getProduct() != null) {
            dto.setProductId(inventory.getProduct().getId());
            dto.setProductName(inventory.getProduct().getName());
            dto.setProductSku(inventory.getProduct().getSku());
            
            // Add category and supplier information if available
            if (inventory.getProduct().getCategory() != null) {
                dto.setProductCategoryName(inventory.getProduct().getCategory().getName());
            }
            
            if (inventory.getProduct().getSupplier() != null) {
                dto.setProductSupplierName(inventory.getProduct().getSupplier().getName());
            }
        }
        
        return dto;
    }
    
    private Inventory convertToEntity(InventoryDTO dto) {
        Inventory inventory = new Inventory();
        inventory.setId(dto.getId());
        inventory.setQuantity(dto.getQuantity());
        inventory.setReorderLevel(dto.getReorderLevel());
        inventory.setReorderQuantity(dto.getReorderQuantity());
        inventory.setLocation(dto.getLocation());
        
        if (dto.getLastUpdated() != null) {
            inventory.setLastUpdated(dto.getLastUpdated());
        } else {
            inventory.setLastUpdated(LocalDateTime.now());
        }
        
        if (dto.getProductId() != null) {
            try {
                Product product = productService.getProductById(dto.getProductId());
                inventory.setProduct(product);
            } catch (RuntimeException e) {
                // Product not found, leave it null
            }
        }
        
        return inventory;
    }
}