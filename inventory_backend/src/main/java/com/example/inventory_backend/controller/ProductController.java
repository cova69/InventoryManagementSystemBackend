// ProductController.java
package com.example.inventory_backend.controller;

import com.example.inventory_backend.dto.ProductDTO;
import com.example.inventory_backend.model.Category;
import com.example.inventory_backend.model.Product;
import com.example.inventory_backend.model.Supplier;
import com.example.inventory_backend.service.CategoryService;
import com.example.inventory_backend.service.NotificationService;
import com.example.inventory_backend.service.ProductService;
import com.example.inventory_backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, SupplierService supplierService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(convertToDTO(product));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable String sku) {
        try {
            Product product = productService.getProductBySku(sku);
            return ResponseEntity.ok(convertToDTO(product));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getProductsByCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return productService.getProductsByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/supplier/{supplierId}")
    public List<ProductDTO> getProductsBySupplier(@PathVariable Long supplierId) {
        Supplier supplier = supplierService.getSupplierById(supplierId);
        return productService.getProductsBySupplier(supplier)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/price")
    public List<ProductDTO> getProductsByMaxPrice(@RequestParam BigDecimal maxPrice) {
        return productService.getProductsByMaxPrice(maxPrice)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productService.saveProduct(product);

        notificationService.notifyNewProduct(savedProduct.getId(), savedProduct.getName());
        
        return new ResponseEntity<>(convertToDTO(savedProduct), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            Product existingProduct = productService.getProductById(id);
            
            // Update product fields
            existingProduct.setName(productDTO.getName());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setSku(productDTO.getSku());
            
            // Update category if provided
            if (productDTO.getCategoryId() != null) {
                Category category = categoryService.getCategoryById(productDTO.getCategoryId());
                existingProduct.setCategory(category);
            } else {
                existingProduct.setCategory(null);
            }
            
            // Update supplier if provided
            if (productDTO.getSupplierId() != null) {
                Supplier supplier = supplierService.getSupplierById(productDTO.getSupplierId());
                existingProduct.setSupplier(supplier);
            } else {
                existingProduct.setSupplier(null);
            }
            
            Product updatedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setSku(product.getSku());
        dto.setPrice(product.getPrice());
        
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        
        if (product.getSupplier() != null) {
            dto.setSupplierId(product.getSupplier().getId());
            dto.setSupplierName(product.getSupplier().getName());
        }
        
        return dto;
    }
    
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSku(dto.getSku());
        product.setPrice(dto.getPrice());
        
        if (dto.getCategoryId() != null) {
            try {
                Category category = categoryService.getCategoryById(dto.getCategoryId());
                product.setCategory(category);
            } catch (RuntimeException e) {
                // Category not found, leave it null
            }
        }
        
        if (dto.getSupplierId() != null) {
            try {
                Supplier supplier = supplierService.getSupplierById(dto.getSupplierId());
                product.setSupplier(supplier);
            } catch (RuntimeException e) {
                // Supplier not found, leave it null
            }
        }
        
        return product;
    }
}