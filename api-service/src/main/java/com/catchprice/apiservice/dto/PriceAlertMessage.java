package com.catchprice.apiservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PriceAlertMessage(
        UUID productId,
        BigDecimal priceFound,
        BigDecimal shipping,
        BigDecimal totalPrice,
        String sourceUrl,
        String marketplace
) {}
