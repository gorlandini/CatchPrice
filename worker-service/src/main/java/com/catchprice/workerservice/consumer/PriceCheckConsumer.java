package com.catchprice.workerservice.consumer;

import com.catchprice.workerservice.client.MercadoLivreClientPort;
import com.catchprice.workerservice.dto.PriceAlertMessage;
import com.catchprice.workerservice.dto.PriceCheckMessage;
import com.catchprice.workerservice.model.PriceHistory;
import com.catchprice.workerservice.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceCheckConsumer {

    private final MercadoLivreClientPort mercadoLivreClient;
    private final KafkaTemplate<String, PriceAlertMessage> kafkaTemplate;
    private final PriceHistoryRepository priceHistoryRepository;

    @KafkaListener(topics = "price-check-requested", groupId = "worker-group", concurrency = "3")
    public void onPriceCheckRequested(PriceCheckMessage msg) {
        mercadoLivreClient.findCheapestWithShipping(msg.mlQuery(), msg.userCep()).ifPresent(result -> {
            try {
                priceHistoryRepository.save(PriceHistory.builder()
                        .productId(msg.productId())
                        .price(result.getPrice())
                        .shipping(result.getShippingCost())
                        .totalPrice(result.getTotalPrice())
                        .sourceUrl(result.getPermalink())
                        .marketplace("MERCADO_LIVRE")
                        .checkedAt(LocalDateTime.now())
                        .build());

                boolean cheaper = msg.referencePrice() == null
                        || result.getTotalPrice().compareTo(msg.referencePrice()) < 0;

                if (cheaper) {
                    kafkaTemplate.send("price-alert-found", msg.productId().toString(),
                            new PriceAlertMessage(
                                    msg.productId(),
                                    result.getPrice(),
                                    result.getShippingCost(),
                                    result.getTotalPrice(),
                                    result.getPermalink(),
                                    "MERCADO_LIVRE"));
                    log.info("Alerta publicado para produto {}: totalPrice={}", msg.productId(), result.getTotalPrice());
                } else {
                    log.info("Sem alerta para produto {}: totalPrice={} >= referencePrice={}",
                            msg.productId(), result.getTotalPrice(), msg.referencePrice());
                }
            } catch (Exception e) {
                log.error("Erro ao processar price check para produto {}: {}", msg.productId(), e.getMessage(), e);
            }
        });
    }
}