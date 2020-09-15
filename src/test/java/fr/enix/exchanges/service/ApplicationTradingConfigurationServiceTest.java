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
    public void testGetEnabledAssetPairsRepresentationForWebServiceTrading() {
        StepVerifier
                .create(applicationTradingConfigurationService.getEnabledAssetPairsRepresentationForWebServiceTrading())
                .expectNext("XLTC/ZEUR")
                .expectNext("XXBT/ZEUR")
                .expectNext("XXRP/ZEUR")
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetEnabledAssetPairsRepresentationForWebSocketTrading() {
        StepVerifier
                .create(applicationTradingConfigurationService.getEnabledAssetPairsRepresentationForWebSocketTrading())
                .expectNext("LTC/EUR")
                .expectNext("XBT/EUR")
                .expectNext("XRP/EUR")
                .expectComplete()
                .verify();
    }
}
