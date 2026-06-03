package com.catchprice.apiservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PriceCheckMessage(
        UUID productId,
        String mlQuery,
        BigDecimal referencePrice,
        String userCep
) {}