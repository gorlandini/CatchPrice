package com.catchprice.workerservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;



@Component
@RequiredArgsConstructor
@Slf4j
public class MercadoLivreClient implements MercadoLivreClientPort {
    private final RestTemplate restTemplate;
    private static final String ML_SEARCH_URL =
            "https://api.mercadolibre.com/sites/MLB/search?q={query}&limit=10";
    private static final String ML_SHIPPING_URL =
            "https://api.mercadolibre.com/items/{itemId}/shipping_options?zip_code={cep}";

    public Optional<MlProductResult> findCheapestWithShipping(String query, String cep) {
        try {
            MlSearchResponse resp = restTemplate.getForObject(ML_SEARCH_URL, MlSearchResponse.class, query);
            if (resp == null || resp.getResults().isEmpty()) return Optional.empty();
            return resp.getResults().stream()
                    .map(item -> enrichWithShipping(item, cep))
                    .min(Comparator.comparing(MlProductResult::getTotalPrice));
        } catch (RestClientException e) {
            log.error("ML API failed for '{}': {}", query, e.getMessage());
            return Optional.empty();
        }
    }

    private MlProductResult enrichWithShipping(MlProductResult item, String cep) {
        try {
            if (item.getShipping().isFreeShipping()) {
                item.setShippingCost(BigDecimal.ZERO);
            } else if (cep != null && !cep.isBlank()) {
                MlShippingResponse shipping = restTemplate.getForObject(
                        ML_SHIPPING_URL, MlShippingResponse.class, item.getId(), cep);
                item.setShippingCost(shipping != null ? shipping.getCheapestOption() : BigDecimal.ZERO);
            }
            item.setTotalPrice(item.getPrice().add(item.getShippingCost()));
        } catch (RestClientException e) {
            log.warn("Shipping lookup failed: {}", e.getMessage());
            item.setShippingCost(BigDecimal.ZERO);
            item.setTotalPrice(item.getPrice());
        }
        return item;
    }
}