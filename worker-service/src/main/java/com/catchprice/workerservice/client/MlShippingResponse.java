package com.catchprice.workerservice.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data; import java.math.BigDecimal;
import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlShippingResponse {
    private List<MlShippingOption> options;
    public BigDecimal getCheapestOption() {
        if (options == null || options.isEmpty()) return BigDecimal.ZERO;
        return options.stream().map(MlShippingOption::getListCost
        )             .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MlShippingOption {
        @JsonProperty("list_cost")
        private BigDecimal listCost;
    }
}
