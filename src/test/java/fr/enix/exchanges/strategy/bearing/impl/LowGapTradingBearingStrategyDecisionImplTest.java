package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@SpringBootTest
class LowGapTradingBearingStrategyDecisionImplTest {

    @Autowired
    private LowGapTradingBearingStrategyDecisionImpl lowGapTradingBearingStrategyDecisionImpl;

    @MockBean
    private PriceReferenceService priceReferenceService;

    @Test
    void testGetDecision_shouldReturnBuyDecisionWithComputedAmountToBuyWhenItIsGreaterOrEqualsThanTheMinimumOrder() {
        final ApplicationAssetPairTicker applicationAssetPairTicker = newApplicationAssetPairTickerForLitecoinEuro(new BigDecimal("42.05"), LocalDateTime.now());
        final LowGapTradingBearingStrategyDecisionImpl lowGapTradingBearingStrategyDecisionSpy = (LowGapTradingBearingStrategyDecisionImpl) spy(lowGapTradingBearingStrategyDecisionImpl);

        doReturn( Mono.just( new BigDecimal("0.1") ) )
        .when   ( lowGapTradingBearingStrategyDecisionSpy).getAmountToBuy(applicationAssetPairTicker);

        StepVerifier
        .create         ( lowGapTradingBearingStrategyDecisionSpy.getDecision(applicationAssetPairTicker) )
        .consumeNextWith( applicationAssetPairTickerTradingDecision -> {
            assertEquals(new BigDecimal("0.1"), applicationAssetPairTickerTradingDecision.getAmount());
        })
        .verifyComplete();
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
