package com.catchprice.workerservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "price_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "product_id", nullable = false)
    private UUID productId;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private BigDecimal shipping;
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
    @Column(name = "source_url")
    private String sourceUrl;
    @Column(nullable = false)
    private String marketplace;
    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;
}
