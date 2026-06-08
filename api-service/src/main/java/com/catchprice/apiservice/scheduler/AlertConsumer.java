package com.catchprice.apiservice.scheduler;

import com.catchprice.apiservice.dto.PriceAlertMessage;
import com.catchprice.apiservice.model.Alert;
import com.catchprice.apiservice.model.Product;
import com.catchprice.apiservice.repositories.AlertRepository;
import com.catchprice.apiservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertConsumer {
    private final AlertRepository alertRepository;
    private final ProductRepository productRepository;

    @KafkaListener(topics = "price-alert-found", groupId = "api-service-group")
    public void onAlertFound(PriceAlertMessage msg) {
        LocalDateTime startOfDay = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        boolean duplicate = alertRepository
                .existsByProductIdAndTotalPriceAndCreatedAtAfter(
                        msg.productId(),
                        msg.totalPrice(),
                        startOfDay);

        if (duplicate) {
            log.info("Alerta ignorado — mesmo preço já registrado hoje para produto {}", msg.productId());
            return;
        }

        Product product = productRepository.findById(msg.productId())
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        alertRepository.save(Alert.builder()
                .product(product)
                .priceFound(msg.priceFound())
                .shipping(msg.shipping())
                .totalPrice(msg.totalPrice())
                .sourceUrl(msg.sourceUrl())
                .marketplace(msg.marketplace())
                .seen(false)
                .build());

        log.info("Alerta persistido para produto {}: totalPrice={}", msg.productId(), msg.totalPrice());
    }
}