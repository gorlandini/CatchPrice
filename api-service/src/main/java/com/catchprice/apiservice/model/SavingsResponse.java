package com.catchprice.apiservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsResponse {
    private String month;
    private Long alertsCount;
    private BigDecimal totalSaved;
    private BigDecimal bestDeal;
    private BigDecimal lowestTotalFound;
    private BigDecimal lowestShippingFound;
}
