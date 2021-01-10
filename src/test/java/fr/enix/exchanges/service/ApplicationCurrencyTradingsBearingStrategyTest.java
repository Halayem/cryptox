package fr.enix.exchanges.service;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsStrategy;
import fr.enix.exchanges.service.impl.ApplicationCurrencyTradingsDynamicBearingStrategyServiceImpl;
import fr.enix.exchanges.service.impl.ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationCurrencyTradingsBearingStrategyTest {

    @Autowired ApplicationCurrencyTradingsBearingStrategy applicationCurrencyTradingsBearingStrategy;

    @Test
    void testGetApplicationCurrencyTradingsBearingStrategyService_shouldReturnInstanceOfStaticBearingStrategy() {
        assertTrue( applicationCurrencyTradingsBearingStrategy
                    .getApplicationCurrencyTradingsBearingStrategyService(ApplicationCurrencyTradingsStrategy.STATIC_BEARING)
                    instanceof ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl
        );
    }

    @Test
    void testGetApplicationCurrencyTradingsBearingStrategyService_shouldReturnInstanceOfDynamicBearingStrategy() {
        assertTrue( applicationCurrencyTradingsBearingStrategy
                    .getApplicationCurrencyTradingsBearingStrategyService(ApplicationCurrencyTradingsStrategy.DYNAMIC_BEARING)
                    instanceof ApplicationCurrencyTradingsDynamicBearingStrategyServiceImpl
        );
    }
}
