package com.example.inventory_backend.service;

import com.example.inventory_backend.dto.InventoryHistoryDTO;
import com.example.inventory_backend.model.Inventory;
import com.example.inventory_backend.model.InventoryHistory;
import com.example.inventory_backend.model.Product;
import com.example.inventory_backend.repository.InventoryHistoryRepository;
import com.example.inventory_backend.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryHistoryService {
    
    @Autowired
    private InventoryHistoryRepository historyRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    public void recordInventoryState() {
        int totalQuantity = calculateTotalQuantity();
        
        InventoryHistory history = new InventoryHistory();
        history.setTimestamp(LocalDateTime.now());
        history.setTotalQuantity(totalQuantity);
        
        historyRepository.save(history);
    }
    
    public void recordProductChange(Product product, Integer quantityChange, String reason) {
        int totalQuantity = calculateTotalQuantity();
        
        InventoryHistory history = new InventoryHistory();
        history.setTimestamp(LocalDateTime.now());
        history.setTotalQuantity(totalQuantity);
        history.setProduct(product);
        history.setQuantityChange(quantityChange);
        history.setChangeReason(reason);
        
        historyRepository.save(history);
    }
    
    public List<InventoryHistoryDTO> getRecentHistory() {
        List<InventoryHistory> history = historyRepository.findTop30ByOrderByTimestampDesc();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return history.stream()
                .map(item -> {
                    InventoryHistoryDTO dto = new InventoryHistoryDTO();
                    dto.setId(item.getId());
                    dto.setTimestamp(item.getTimestamp());
                    dto.setTotalQuantity(item.getTotalQuantity());
                    dto.setFormattedTimestamp(item.getTimestamp().format(formatter));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    private int calculateTotalQuantity() {
        return inventoryRepository.findAll().stream()
                .mapToInt(Inventory::getQuantity)
                .sum();
    }
}