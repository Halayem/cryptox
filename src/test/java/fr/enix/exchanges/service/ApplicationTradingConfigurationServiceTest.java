package fr.enix.exchanges.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class ApplicationTradingConfigurationServiceTest {

    @Autowired
    private ApplicationTradingConfigurationService applicationTradingConfigurationService;

    @Test
    public void testGetEnabledAssetPairsForTrading() {
        StepVerifier
                .create(applicationTradingConfigurationService.getEnabledAssetPairsForTrading())
                .expectNext("XLTC/ZEUR")
                .expectNext("XXBT/ZEUR")
                .expectNext("XXRP/ZEUR")
                .expectComplete()
                .verify();
    }
}
