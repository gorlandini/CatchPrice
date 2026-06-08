package com.catchprice.workerservice.client;



import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Primary
@Profile("dev")
@Slf4j
public class MercadoLivreClientMock implements MercadoLivreClientPort {

    @Override
    public Optional<MlProductResult> findCheapestWithShipping(String query, String cep) {
        log.info("[MOCK] ML query='{}' cep='{}' — retornando produto fictício", query, cep);

        MlProductResult.MlShipping shipping = new MlProductResult.MlShipping();
        shipping.setFreeShipping(false);

        MlProductResult product = new MlProductResult();
        product.setId("MLB-MOCK-001");


        product.setPrice(new BigDecimal("20.0"));
        product.setShippingCost(new BigDecimal("15.00"));
        product.setTotalPrice(new BigDecimal("35.00"));
        product.setPermalink("https://www.mercadolivre.com.br/mock");
        product.setShipping(shipping);

        return Optional.of(product);
    }
}
