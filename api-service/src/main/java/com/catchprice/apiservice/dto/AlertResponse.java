package com.catchprice.apiservice.dto;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AlertResponse(
        UUID id,
        UUID productId,
        String productName,
        BigDecimal priceFound,
        BigDecimal shipping,
        BigDecimal totalPrice,
        String sourceUrl,
        String marketplace,
        boolean seen,
        LocalDateTime createdAt
) {}
