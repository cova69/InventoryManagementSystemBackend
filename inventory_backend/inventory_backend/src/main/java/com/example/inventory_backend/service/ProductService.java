// ProductService.java
package com.example.inventory_backend.service;

import com.example.inventory_backend.model.Category;
import com.example.inventory_backend.model.Product;
import com.example.inventory_backend.model.Supplier;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    
    Product getProductById(Long id);
    
    Product getProductBySku(String sku);
    
    List<Product> getProductsByCategory(Category category);
    
    List<Product> getProductsBySupplier(Supplier supplier);
    
    List<Product> searchProductsByName(String name);
    
    List<Product> getProductsByMaxPrice(BigDecimal maxPrice);
    
    Product saveProduct(Product product);
    
    void deleteProduct(Long id);
}
