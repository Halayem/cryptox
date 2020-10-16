package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DoNothingTradingBearingStrategyDecisionImplTest {

    @Autowired private DoNothingTradingBearingStrategyDecisionImpl doNothingTradingBearingStrategyDecision;

    @Test
    void testGetDecision() {
        final ApplicationAssetPairTicker applicationAssetPairTicker = newApplicationAssetPairTickerForLitecoinEuro(new BigDecimal("42.05"), LocalDateTime.now());

        StepVerifier
        .create(doNothingTradingBearingStrategyDecision.getDecision(applicationAssetPairTicker))
        .consumeNextWith(applicationAssetPairTickerTradingDecision -> {
            assertNull(applicationAssetPairTickerTradingDecision.getAmount(), "amount should be null for do nothing decision");
            assertNull(applicationAssetPairTickerTradingDecision.getPrice(),  "price should be null for do nothing decision");

            assertEquals("price: <42,050000> (litecoin-euro) did not reach the gap", applicationAssetPairTickerTradingDecision.getOperation().getMessage());
            assertEquals(applicationAssetPairTicker, applicationAssetPairTickerTradingDecision.getApplicationAssetPairTickerReference());
            assertEquals(ApplicationAssetPairTickerTradingDecision.Decision.DO_NOTHING, applicationAssetPairTickerTradingDecision.getOperation().getDecision());
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
