package com.example.inventory_backend.service.impl;

import com.example.inventory_backend.dto.TransactionDTO;
import com.example.inventory_backend.model.Transaction;
import com.example.inventory_backend.model.Product;
import com.example.inventory_backend.model.User;
import com.example.inventory_backend.repository.TransactionRepository;
import com.example.inventory_backend.repository.ProductRepository;
import com.example.inventory_backend.repository.UserRepository;
import com.example.inventory_backend.service.TransactionService;
import com.example.inventory_backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InventoryService inventoryService;

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    @Override
    public List<Transaction> getTransactionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        return transactionRepository.findByProduct(product);
    }

    @Override
    public List<Transaction> getTransactionsByType(Transaction.TransactionType type) {
        return transactionRepository.findByType(type);
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTransactionDateBetween(start, end);
    }

    @Override
    @Transactional
    public Transaction saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        
        // Set product
        Product product = productRepository.findById(transactionDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + transactionDTO.getProductId()));
        transaction.setProduct(product);
        
        // Set transaction type
        transaction.setType(Transaction.TransactionType.valueOf(transactionDTO.getTransactionType()));
        
        // Set quantity and date
        transaction.setQuantity(transactionDTO.getQuantity());
        transaction.setTransactionDate(transactionDTO.getTransactionDate() != null ? 
                transactionDTO.getTransactionDate() : LocalDateTime.now());
        
        // Set price information
        transaction.setUnitPrice(transactionDTO.getUnitPrice());
        
        // Calculate total amount if not provided
        if (transactionDTO.getTotalAmount() == null && transactionDTO.getUnitPrice() != null) {
            BigDecimal total = transactionDTO.getUnitPrice().multiply(
                    new BigDecimal(transactionDTO.getQuantity()));
            transaction.setTotalAmount(total);
        } else {
            transaction.setTotalAmount(transactionDTO.getTotalAmount());
        }
        
        // Set other fields
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setReferenceNumber(transactionDTO.getReferenceNumber());
        
        // Set user if provided
        if (transactionDTO.getUserId() != null) {
            User user = userRepository.findById(transactionDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + transactionDTO.getUserId()));
            transaction.setCreatedBy(user);
        }
        
        // Update inventory based on transaction type
        updateInventory(transaction);
        
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
    
    private void updateInventory(Transaction transaction) {
        int quantityChange = 0;
        
        switch (transaction.getType()) {
            case PURCHASE:
            case RETURN:
                // Increase inventory
                quantityChange = transaction.getQuantity();
                break;
            case SALE:
                // Decrease inventory
                quantityChange = -transaction.getQuantity();
                break;
            case ADJUSTMENT:
                // Use the actual quantity value (could be positive or negative)
                quantityChange = transaction.getQuantity();
                break;
            case TRANSFER:
                // For transfers, handle separately if needed
                // This might involve multiple inventory updates
                break;
        }
        
        if (quantityChange != 0) {
            inventoryService.updateQuantity(transaction.getProduct().getId(), quantityChange);
        }
    }
}