package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@SpringBootTest
class HighGapTradingBearingStrategyDecisionImplTest {

    @Autowired private HighGapTradingBearingStrategyDecisionImpl highGapTradingBearingStrategyDecision;

    @MockBean private PriceReferenceService priceReferenceService;
    @MockBean private ExchangeService       exchangeService;

    @Test
    void testGetDecision_shouldReturnSellDecisionWithComputedAmountToSellWhenItIsGreaterOrEqualsThanTheMinimumOrder() {
        final ApplicationAssetPairTicker applicationAssetPairTicker = newApplicationAssetPairTickerForLitecoinEuro(new BigDecimal("42.05"), LocalDateTime.now());
        final HighGapTradingBearingStrategyDecisionImpl highGapTradingBearingStrategyDecisionSpy = spy(highGapTradingBearingStrategyDecision);

        doReturn( Mono.just( new BigDecimal("0.1") ) )
        .when   ( highGapTradingBearingStrategyDecisionSpy).getAmountToSell("litecoin-euro");

        StepVerifier
        .create         ( highGapTradingBearingStrategyDecisionSpy.getDecision(applicationAssetPairTicker) )
        .consumeNextWith( applicationAssetPairTickerTradingDecision -> {
            assertEquals(ApplicationAssetPairTickerTradingDecision.Decision.SELL, applicationAssetPairTickerTradingDecision.getOperation().getDecision());
            assertEquals(new BigDecimal("0.1"), applicationAssetPairTickerTradingDecision.getAmount());
            assertEquals(new BigDecimal("42.05"), applicationAssetPairTickerTradingDecision.getPrice());
            assertEquals(applicationAssetPairTicker, applicationAssetPairTickerTradingDecision.getApplicationAssetPairTickerReference());
            assertNull(applicationAssetPairTickerTradingDecision.getOperation().getMessage());
        })
        .verifyComplete();
    }

    @Test
    void testGetDecision_shouldReturnDoNothingDecisionWhenComputedAmountIsLessThanTheMinimumOrder() {
        final ApplicationAssetPairTicker applicationAssetPairTicker = newApplicationAssetPairTickerForLitecoinEuro(new BigDecimal("42.05"), LocalDateTime.now());
        final HighGapTradingBearingStrategyDecisionImpl highGapTradingBearingStrategyDecisionSpy = spy(highGapTradingBearingStrategyDecision);

        doReturn( Mono.just( new BigDecimal("0.099999") ) )
                .when   ( highGapTradingBearingStrategyDecisionSpy).getAmountToSell("litecoin-euro");

        StepVerifier
        .create         ( highGapTradingBearingStrategyDecisionSpy.getDecision(applicationAssetPairTicker) )
        .consumeNextWith( applicationAssetPairTickerTradingDecision -> {
            assertNull(applicationAssetPairTickerTradingDecision.getAmount(), "amount should be null for do nothing decision");
            assertNull(applicationAssetPairTickerTradingDecision.getPrice(),  "price should be null for do nothing decision");

            assertEquals("the computed amount to sell: <0,099999>, is less than the minimum order by market", applicationAssetPairTickerTradingDecision.getOperation().getMessage());
            assertEquals(applicationAssetPairTicker, applicationAssetPairTickerTradingDecision.getApplicationAssetPairTickerReference());
            assertEquals(ApplicationAssetPairTickerTradingDecision.Decision.DO_NOTHING, applicationAssetPairTickerTradingDecision.getOperation().getDecision());
        })
        .verifyComplete();
    }

    @Test
    void testGetAmountToSell_shouldReturnTheConfiguredAmountWhenBuyIsPossible() {
        final ApplicationAssetPairTicker applicationAssetPairTicker = newApplicationAssetPairTickerForLitecoinEuro(new BigDecimal("42.05"), LocalDateTime.now());

        doReturn( Mono.just(new BigDecimal("5")))
        .when   (exchangeService).getAvailableAssetForSellPlacementByApplicationAssetPair("litecoin-euro");

        StepVerifier
        .create         (highGapTradingBearingStrategyDecision.getAmountToSell("litecoin-euro"))
        .consumeNextWith(amountToSell -> assertEquals(new BigDecimal("0.1"), amountToSell));
    }

    @Test
    void testGetAmountToSell_shouldReturnTheComputedAmountWhenSellConfiguredAmountIsNotPossible() {
        final ApplicationAssetPairTicker applicationAssetPairTicker = newApplicationAssetPairTickerForLitecoinEuro(new BigDecimal("40"), LocalDateTime.now());

        doReturn( Mono.just(new BigDecimal("0.099999")))
        .when   (exchangeService).getAvailableAssetForSellPlacementByApplicationAssetPair("litecoin-euro");

        StepVerifier
        .create         ( highGapTradingBearingStrategyDecision.getAmountToSell("litecoin-euro") )
        .consumeNextWith( amountToSell -> assertEquals(new BigDecimal("0.099999"), amountToSell) );
    }

    private ApplicationAssetPairTicker newApplicationAssetPairTickerForLitecoinEuro(final BigDecimal price,
                                                                                    final LocalDateTime dateTime) {
        return ApplicationAssetPairTicker
                .builder()
                .applicationAssetPair   ( "litecoin-euro"   )
                .price                  ( price             )
                .dateTime               ( dateTime          )
                .build();
    }
}
