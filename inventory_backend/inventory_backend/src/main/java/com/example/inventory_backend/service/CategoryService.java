// CategoryService.java
package com.example.inventory_backend.service;

import com.example.inventory_backend.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    
    Category getCategoryById(Long id);
    
    Category getCategoryByName(String name);
    
    Category saveCategory(Category category);
    
    void deleteCategory(Long id);
}
