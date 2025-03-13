package com.example.inventory_backend.controller;

import com.example.inventory_backend.dto.InventoryHistoryDTO;
import com.example.inventory_backend.service.InventoryHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-history")
@CrossOrigin(origins = "http://localhost:3000", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class InventoryHistoryController {
    
    @Autowired
    private InventoryHistoryService historyService;
    
    @GetMapping("/recent")
    public List<InventoryHistoryDTO> getRecentHistory() {
        return historyService.getRecentHistory();
    }
}