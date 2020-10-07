package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.OrderType;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
class TickerServiceImplTest {

    @Autowired private TickerServiceImpl tickerService;

    @Test
    void testNewAddOrderInputForBuyPlacement_shouldBeOk() {
        StepVerifier
        .create(tickerService.newAddOrderInputForBuyPlacement(
                    ApplicationAssetPairTickerTradingDecision
                    .builder    ()
                    .amount     (new BigDecimal("0.5"))
                    .price      (new BigDecimal("45.12524585"))
                    .operation  (
                        ApplicationAssetPairTickerTradingDecision
                        .Operation
                        .builder()
                        .decision(ApplicationAssetPairTickerTradingDecision.Decision.BUY)
                        .build()
                    )
                    .applicationAssetPairTickerReference(
                        ApplicationAssetPairTicker
                        .builder()
                        .applicationAssetPair("litecoin-euro")
                        .build()
                    )
                    .build()
                )
        )
        .consumeNextWith(addOrderInput -> {
            assertEquals("litecoin-euro", addOrderInput.getApplicationAssetPair());
            assertEquals(new BigDecimal("0.5"), addOrderInput.getVolume());
            assertEquals(new BigDecimal("45.12524585"), addOrderInput.getPrice());
            assertEquals(AddOrderType.BUY, addOrderInput.getAddOrderType());
            assertEquals(OrderType.LIMIT, addOrderInput.getOrderType() );
        })
        .verifyComplete();
    }

    @Test
    void testNewAddOrderInputForSellPlacement_shouldBeOk() {
        StepVerifier
        .create(tickerService.newAddOrderInputForSellPlacement(
                ApplicationAssetPairTickerTradingDecision
                .builder    ()
                .amount     (new BigDecimal("0.5"))
                .price      (new BigDecimal("45.12524585"))
                .operation  (
                        ApplicationAssetPairTickerTradingDecision
                        .Operation
                        .builder()
                        .decision(ApplicationAssetPairTickerTradingDecision.Decision.SELL)
                        .build()
                )
                .applicationAssetPairTickerReference(
                        ApplicationAssetPairTicker
                        .builder()
                        .applicationAssetPair("litecoin-euro")
                        .build()
                )
                .build()
            )
        )
        .consumeNextWith(addOrderInput -> {
            assertEquals("litecoin-euro", addOrderInput.getApplicationAssetPair());
            assertEquals(new BigDecimal("0.5"), addOrderInput.getVolume());
            assertEquals(new BigDecimal("45.12524585"), addOrderInput.getPrice());
            assertEquals(AddOrderType.SELL, addOrderInput.getAddOrderType());
            assertEquals(OrderType.LIMIT, addOrderInput.getOrderType() );
        })
        .verifyComplete();
    }
}
