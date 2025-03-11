// SupplierService.java
package com.example.inventory_backend.service;

import com.example.inventory_backend.model.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> getAllSuppliers();
    
    Supplier getSupplierById(Long id);
    
    List<Supplier> searchSuppliersByName(String name);
    
    Supplier saveSupplier(Supplier supplier);
    
    void deleteSupplier(Long id);
}
