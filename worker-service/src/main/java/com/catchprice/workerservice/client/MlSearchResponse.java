package com.catchprice.workerservice.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data; import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlSearchResponse {
    private List<MlProductResult> results;
}
