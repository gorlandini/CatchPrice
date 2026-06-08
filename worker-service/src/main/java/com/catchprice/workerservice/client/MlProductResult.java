package com.catchprice.workerservice.client;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data; import java.math.BigDecimal;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlProductResult {
    private String id;
    private BigDecimal price;
    private String permalink;
    private MlShipping shipping;
    private BigDecimal shippingCost;
    // calculado pelo worker
    private BigDecimal totalPrice;
    // calculado pelo worker
    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MlShipping {
        @JsonProperty("free_shipping")
        private boolean freeShipping;     } }
