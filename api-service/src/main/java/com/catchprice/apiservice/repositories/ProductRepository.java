package com.catchprice.apiservice.repositories;

import com.catchprice.apiservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByUserEmail(String email, Pageable pageable);
    Page<Product> findByUserId(UUID id, Pageable pageable);
    List<Product> findByActiveTrue();



}
