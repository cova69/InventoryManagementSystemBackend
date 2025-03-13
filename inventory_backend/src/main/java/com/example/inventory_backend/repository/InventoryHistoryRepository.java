package com.example.inventory_backend.repository;

import com.example.inventory_backend.model.InventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Long> {
    List<InventoryHistory> findTop30ByOrderByTimestampDesc();
    List<InventoryHistory> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}