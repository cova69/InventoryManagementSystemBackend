package com.example.inventory_backend.service.impl;

import com.example.inventory_backend.model.Product;
import com.example.inventory_backend.model.Transaction;
import com.example.inventory_backend.model.Transaction.TransactionType;
import com.example.inventory_backend.repository.TransactionRepository;
import com.example.inventory_backend.service.InventoryService;
import com.example.inventory_backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final InventoryService inventoryService;
    
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, InventoryService inventoryService) {
        this.transactionRepository = transactionRepository;
        this.inventoryService = inventoryService;
    }
    
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
    public List<Transaction> getTransactionsByProduct(Product product) {
        return transactionRepository.findByProduct(product);
    }
    
    @Override
    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByType(type);
    }
    
    @Override
    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTransactionDateBetween(start, end);
    }
    
    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        // Set transaction date if not already set
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }
        
        // Update inventory based on transaction type
        Integer quantityChange = 0;
        
        switch (transaction.getType()) {
            case PURCHASE:
            case RETURN:
                quantityChange = transaction.getQuantity(); // Increase inventory
                break;
            case SALE:
                // Check if enough stock is available
                if (!inventoryService.isInStock(transaction.getProduct().getId(), transaction.getQuantity())) {
                    throw new RuntimeException("Not enough stock available for product: " + transaction.getProduct().getName());
                }
                quantityChange = -transaction.getQuantity(); // Decrease inventory
                break;
            case ADJUSTMENT:
                // For adjustments, quantity could be positive or negative already
                quantityChange = transaction.getQuantity();
                break;
            case TRANSFER:
                // For transfers, handle separately if needed
                break;
        }
        
        // Update inventory if quantity change is not zero
        if (quantityChange != 0) {
            inventoryService.updateQuantity(transaction.getProduct().getId(), quantityChange);
        }
        
        return transactionRepository.save(transaction);
    }
    
    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}