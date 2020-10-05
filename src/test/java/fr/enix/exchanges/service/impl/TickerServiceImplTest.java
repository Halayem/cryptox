package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.OrderType;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.service.TradingDecisionService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
class TickerServiceImplTest {

    @Autowired private TickerServiceImpl tickerService;
    @MockBean private TradingDecisionService tradingDecisionService;

    @Test
    void testNewAddOrderInputForBuyPlacement_shouldBeOk() {
        Mockito
        .when       ( tradingDecisionService.getAmountToBuy( Mockito.any(ApplicationAssetPairTicker.class) ))
        .thenReturn ( Mono.just(new BigDecimal("0.5")) );

        StepVerifier
        .create(tickerService.newAddOrderInputForBuyPlacement(
                    ApplicationAssetPairTicker
                    .builder()
                    .applicationAssetPair   ("litecoin-euro")
                    .price                  (new BigDecimal("45.12524585"))
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
    void testNewAddOrderInputForBuyPlacement_shouldReturnMonoEmptyWhenAmountToBuyEqualsZero() {
        Mockito
        .when       ( tradingDecisionService.getAmountToBuy( Mockito.any(ApplicationAssetPairTicker.class) ))
        .thenReturn ( Mono.just(new BigDecimal("0")) );

        StepVerifier
        .create(tickerService.newAddOrderInputForBuyPlacement(
                    ApplicationAssetPairTicker
                    .builder()
                    .applicationAssetPair   ("litecoin-euro")
                    .price                  (new BigDecimal("45.12524585"))
                    .build()
                )
        )
        .verifyComplete();
    }

    @Test
    void testNewAddOrderInputForBuyPlacement_shouldReturnMonoEmptyWhenAmountToBuyIsLessThanZero() {
        Mockito
        .when       ( tradingDecisionService.getAmountToBuy( Mockito.any(ApplicationAssetPairTicker.class) ))
        .thenReturn ( Mono.just(new BigDecimal("-0.00000001")) );

        StepVerifier
        .create(tickerService.newAddOrderInputForBuyPlacement(
                    ApplicationAssetPairTicker
                    .builder()
                    .applicationAssetPair   ("litecoin-euro")
                    .price                  (new BigDecimal("45.12524585"))
                    .build()
                )
        )
        .verifyComplete();
    }

    @Test
    void testNewAddOrderInputForSellPlacement_shouldBeOk() {
        Mockito
        .when       ( tradingDecisionService.getAmountToSell( Mockito.any(String.class) ))
        .thenReturn ( Mono.just(new BigDecimal("0.5")) );

        StepVerifier
        .create(tickerService.newAddOrderInputForSellPlacement(
                    ApplicationAssetPairTicker
                    .builder()
                    .applicationAssetPair   ("litecoin-euro")
                    .price                  (new BigDecimal("45.12524585"))
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

    @Test
    void testNewAddOrderInputForSellPlacement_shouldReturnMonoEmptyWhenAmountToSellEqualsZero() {
        Mockito
        .when       ( tradingDecisionService.getAmountToSell( Mockito.any(String.class) ))
        .thenReturn ( Mono.just(new BigDecimal("0")) );

        StepVerifier
        .create(tickerService.newAddOrderInputForSellPlacement(
                    ApplicationAssetPairTicker
                    .builder()
                    .applicationAssetPair   ("litecoin-euro")
                    .price                  (new BigDecimal("45.12524585"))
                    .build()
                )
        )
        .verifyComplete();
    }

    @Test
    void testNewAddOrderInputForSellPlacement_shouldReturnMonoEmptyWhenAmountToSellIsLessThanZero() {
        Mockito
        .when       ( tradingDecisionService.getAmountToSell( Mockito.any(String.class) ))
        .thenReturn ( Mono.just(new BigDecimal("-0.00000001")) );

        StepVerifier
        .create(tickerService.newAddOrderInputForSellPlacement(
                    ApplicationAssetPairTicker
                    .builder()
                    .applicationAssetPair   ("litecoin-euro")
                    .price                  (new BigDecimal("45.12524585"))
                    .build()
                )
        )
        .verifyComplete();
    }

}
