package com.catchprice.apiservice.dto;

import com.catchprice.apiservice.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProductResponse(
        UUID id,
        String name,
        String mlQuery,
        BigDecimal referencePrice,
        boolean active,
        LocalDateTime createdAt
) {
    public static CreateProductResponse fromEntity(Product p) {
        return new CreateProductResponse(
                p.getId(), p.getName(), p.getMlQuery(),
                p.getReferencePrice(), p.isActive(), p.getCreatedAt()
        );
    }
}
