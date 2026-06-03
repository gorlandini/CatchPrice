package com.catchprice.apiservice.dto;

import java.math.BigDecimal;

public interface SavingsResponse {
    String getMonth();
    Long getAlertsCount();
    BigDecimal getTotalSaved();
    BigDecimal getBestDeal();
    BigDecimal getLowestTotalFound();
    BigDecimal getLowestShippingFound();
}