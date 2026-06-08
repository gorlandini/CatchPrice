package com.catchprice.workerservice;
import com.catchprice.workerservice.client.MercadoLivreClient;
import com.catchprice.workerservice.client.MlProductResult;
import com.catchprice.workerservice.consumer.PriceCheckConsumer;
import com.catchprice.workerservice.dto.PriceAlertMessage;
import com.catchprice.workerservice.dto.PriceCheckMessage;
import com.catchprice.workerservice.repository.PriceHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceCheckConsumerTest {
    @Mock
    MercadoLivreClient mlClient;
    @Mock KafkaTemplate<String, PriceAlertMessage> kafkaTemplate;
    @Mock
    PriceHistoryRepository priceHistoryRepo;
    @InjectMocks
    PriceCheckConsumer consumer;

    @Test
    void shouldPublishAlertWhenTotalPriceIsLower() {
        UUID id = UUID.randomUUID();
        var msg = new PriceCheckMessage(id, "smart tv 55", new BigDecimal("1500.00"), "01310100");
        var item = buildItem("1400.00", "50.00", "1450.00");
        when(mlClient.findCheapestWithShipping("smart tv 55", "01310100")).thenReturn(Optional.of(item));

        consumer.onPriceCheckRequested(msg);

        verify(kafkaTemplate).send(eq("price-alert-found"), eq(id.toString()),
                argThat(a -> a.totalPrice().compareTo(new BigDecimal("1450.00")) == 0));
    }

    @Test
    void shouldNotPublishAlertWhenTotalPriceExceedsReferenceAfterShipping() {
        // R$1.400 + frete R$150 = R$1.550 > referência R$1.500 — sem alerta
        UUID id = UUID.randomUUID();
        var msg = new PriceCheckMessage(id, "smart tv 55", new BigDecimal("1500.00"), "01310100");
        var item = buildItem("1400.00", "150.00", "1550.00");
        when(mlClient.findCheapestWithShipping("smart tv 55", "01310100")).thenReturn(Optional.of(item));

        consumer.onPriceCheckRequested(msg);

        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void shouldPublishAlertWhenReferencePriceIsNull() {
        UUID id = UUID.randomUUID();
        var msg = new PriceCheckMessage(id, "smart tv 55", null, "01310100");
        var item = buildItem("1400.00", "0.00", "1400.00");
        when(mlClient.findCheapestWithShipping("smart tv 55", "01310100")).thenReturn(Optional.of(item));

        consumer.onPriceCheckRequested(msg);

        verify(kafkaTemplate).send(eq("price-alert-found"), any(), any());
    }

    @Test
    void shouldNotPublishAlertWhenMlApiFails() {
        var msg = new PriceCheckMessage(UUID.randomUUID(), "smart tv 55", new BigDecimal("1500.00"), "01310100");
        when(mlClient.findCheapestWithShipping(any(), any())).thenReturn(Optional.empty());

        consumer.onPriceCheckRequested(msg);

        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void shouldAlwaysPersistPriceHistoryEvenWhenNotCheaper() {
        var msg = new PriceCheckMessage(UUID.randomUUID(), "smart tv 55", new BigDecimal("1000.00"), "01310100");
        var item = buildItem("1400.00", "0.00", "1400.00");
        when(mlClient.findCheapestWithShipping("smart tv 55", "01310100")).thenReturn(Optional.of(item));

        consumer.onPriceCheckRequested(msg);

        verify(priceHistoryRepo).save(any());
        verifyNoInteractions(kafkaTemplate);
    }

    private MlProductResult buildItem(String price, String shipping, String total) {
        var item = new MlProductResult();
        item.setPrice(new BigDecimal(price));
        item.setShippingCost(new BigDecimal(shipping));
        item.setTotalPrice(new BigDecimal(total));
        item.setPermalink("https://ml.com/p/1");
        return item;
    }
}