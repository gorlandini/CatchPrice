package com.catchprice.apiservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


public record CreateProductRequest (
    @NotBlank
    String name,
    @NotBlank
    String mlQuery,
    @NotNull
    @DecimalMin("0.01")
    BigDecimal referencePrice
){}
