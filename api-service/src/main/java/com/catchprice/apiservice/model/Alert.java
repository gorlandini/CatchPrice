package com.catchprice.apiservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id") private Product product;
    @Column(name = "price_found", nullable = false) private BigDecimal priceFound;
    @Column(nullable = false) private BigDecimal shipping;
    @Column(name = "total_price", nullable = false) private BigDecimal totalPrice;
    @Column(name = "source_url") private String sourceUrl;
    @Column(nullable = false) private String marketplace;
    @Column(nullable = false) private boolean seen = false;
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
}