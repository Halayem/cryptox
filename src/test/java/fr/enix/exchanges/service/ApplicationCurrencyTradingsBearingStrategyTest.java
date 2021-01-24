package fr.enix.exchanges.service;

import fr.enix.exchanges.service.impl.ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationCurrencyTradingsBearingStrategyTest {

    @Autowired ApplicationCurrencyTradingsBearingStrategy applicationCurrencyTradingsBearingStrategy;

    @Test
    void testGetApplicationCurrencyTradingsBearingStrategyService_shouldReturnInstanceOfStaticBearingStrategyWhenApplicationAssetPairIsLitecoinEuro() {
        StepVerifier
        .create(
            applicationCurrencyTradingsBearingStrategy
            .getApplicationCurrencyTradingsBearingStrategyService("litecoin-euro"))
        .consumeNextWith(applicationCurrencyTradingsBearingStrategyService ->
            assertTrue( applicationCurrencyTradingsBearingStrategyService
                        instanceof ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl)
            )
        .verifyComplete();
    }

}
