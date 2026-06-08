package com.catchprice.apiservice.repositories;

import com.catchprice.apiservice.model.Alert;
import com.catchprice.apiservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
    Page<Alert> findByProductUserEmailOrderByCreatedAtDesc(String email, Pageable pageable);
    long countByProductUserEmailAndSeenFalse(String email);
    List<Alert> findByProductUserEmailOrderByCreatedAtDesc(String email);
    boolean existsByProductIdAndTotalPriceAndCreatedAtAfter(UUID productId, BigDecimal totalPrice, LocalDateTime after);
}
