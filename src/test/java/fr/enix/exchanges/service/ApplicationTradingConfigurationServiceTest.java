package fr.enix.exchanges.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class ApplicationTradingConfigurationServiceTest {

    @Autowired
    private ApplicationTradingConfigurationService applicationTradingConfigurationService;

    @Test
    void testGetEnabledAssetPairsRepresentationForWebServiceTrading() {
        StepVerifier
                .create(applicationTradingConfigurationService.getEnabledAssetPairsRepresentationForWebServiceTrading())
                .expectNext("XLTCZEUR")
                .expectNext("XXBTZEUR")
                .expectNext("XXRPZEUR")
                .expectComplete()
                .verify();
    }

    @Test
    void testGetEnabledAssetPairsRepresentationForWebSocketTrading() {
        StepVerifier
                .create(applicationTradingConfigurationService.getEnabledAssetPairsRepresentationForWebSocketTrading())
                .expectNext("LTC/EUR")
                .expectNext("XBT/EUR")
                .expectNext("XRP/EUR")
                .expectComplete()
                .verify();
    }
}
