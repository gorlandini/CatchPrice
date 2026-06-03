package com.catchprice.apiservice.scheduler;

import com.catchprice.apiservice.dto.PriceCheckMessage;
import com.catchprice.apiservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceCheckScheduler {
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, PriceCheckMessage> kafkaTemplate;

    @Scheduled(cron = "${scheduler.cron}")
    public void triggerDailyCheck() {
        log.info("Starting daily price check");
        productRepository.findByActiveTrue().forEach(product ->
                kafkaTemplate.send("price-check-requested", product.getId().toString(),
                        new PriceCheckMessage(product.getId(), product.getMlQuery(),
                                product.getReferencePrice(), product.getUser().getCep())));
    }
}
