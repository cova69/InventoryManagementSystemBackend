// SupplierController.java
package com.example.inventory_backend.controller;

import com.example.inventory_backend.dto.SupplierDTO;
import com.example.inventory_backend.model.Supplier;
import com.example.inventory_backend.service.NotificationService;
import com.example.inventory_backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "http://localhost:3000")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Autowired
    private NotificationService notificationService;


    @GetMapping
    public List<SupplierDTO> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return suppliers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long id) {
        try {
            Supplier supplier = supplierService.getSupplierById(id);
            return ResponseEntity.ok(convertToDTO(supplier));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public List<SupplierDTO> searchSuppliers(@RequestParam String name) {
        return supplierService.searchSuppliersByName(name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierDTO supplierDTO) {
        Supplier supplier = convertToEntity(supplierDTO);
        Supplier savedSupplier = supplierService.saveSupplier(supplier);

        notificationService.notifyNewSupplier(savedSupplier.getId(), savedSupplier.getName());
        
        return new ResponseEntity<>(convertToDTO(savedSupplier), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, @RequestBody SupplierDTO supplierDTO) {
        try {
            Supplier existingSupplier = supplierService.getSupplierById(id);
            
            existingSupplier.setName(supplierDTO.getName());
            existingSupplier.setContactName(supplierDTO.getContactName());
            existingSupplier.setEmail(supplierDTO.getEmail());
            existingSupplier.setPhone(supplierDTO.getPhone());
            existingSupplier.setAddress(supplierDTO.getAddress());
            
            Supplier updatedSupplier = supplierService.saveSupplier(existingSupplier);
            return ResponseEntity.ok(convertToDTO(updatedSupplier));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private SupplierDTO convertToDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setContactName(supplier.getContactName());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setAddress(supplier.getAddress());
        dto.setProductCount(supplier.getProducts() != null ? supplier.getProducts().size() : 0);
        return dto;
    }
    
    private Supplier convertToEntity(SupplierDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
        supplier.setName(dto.getName());
        supplier.setContactName(dto.getContactName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());
        return supplier;
    }
}