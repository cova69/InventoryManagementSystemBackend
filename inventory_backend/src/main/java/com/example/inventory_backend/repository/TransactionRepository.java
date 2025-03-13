package com.example.inventory_backend.repository;

import com.example.inventory_backend.model.Transaction;
import com.example.inventory_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByProduct(Product product);
    List<Transaction> findByType(Transaction.TransactionType type);
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
    List<Transaction> findByProductAndTransactionDateBetween(Product product, LocalDateTime start, LocalDateTime end);
}