package com.example.inventory_backend.service;

import com.example.inventory_backend.model.Product;
import com.example.inventory_backend.model.Transaction;
import com.example.inventory_backend.model.Transaction.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();
    
    Transaction getTransactionById(Long id);
    
    List<Transaction> getTransactionsByProduct(Product product);
    
    List<Transaction> getTransactionsByType(TransactionType type);
    
    List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);
    
    Transaction createTransaction(Transaction transaction);
    
    void deleteTransaction(Long id);
}
