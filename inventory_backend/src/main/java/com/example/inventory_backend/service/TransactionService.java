package com.example.inventory_backend.service;

import com.example.inventory_backend.dto.TransactionDTO;
import com.example.inventory_backend.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(Long id);
    List<Transaction> getTransactionsByProductId(Long productId);
    List<Transaction> getTransactionsByType(Transaction.TransactionType type);
    List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);
    Transaction saveTransaction(TransactionDTO transactionDTO);
    void deleteTransaction(Long id);
}