package com.catchprice.apiservice.repositories;

import com.catchprice.apiservice.model.Alert;
import com.catchprice.apiservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
    Page<Alert> findByProductUserEmailOrderByCreatedAtDesc(String email, Pageable pageable);
    long countByProductUserEmailAndSeenFalse(String email);
}
