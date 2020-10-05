package fr.enix.exchanges.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class ApplicationCurrencyTradingsParameterRepositoryTest {

    @Autowired
    private ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    @Test
    void testGetEnabledApplicationAssetPairForTrading() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getEnabledApplicationAssetPairForTrading())
                .expectNext("litecoin-euro")
                .expectNext("bitcoin-euro")
                .expectNext("ripple-euro")
                .expectComplete()
                .verify();
    }

    @Test
    void testGetStrategiesByApplicationAssetPair_litecoinToEuro() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getStrategiesByApplicationAssetPair("litecoin-euro"))
                .expectNext("bearing")
                .expectComplete()
                .verify();
    }

    @Test
    void testGetStrategiesByApplicationAssetPair_bitcoinToEuro() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getStrategiesByApplicationAssetPair("bitcoin-euro"))
                .expectNext("bearing")
                .expectNext("threshold")
                .expectComplete()
                .verify();
    }

    @Test
    void testGetStrategiesByApplicationAssetPair_rippleToEuro() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getStrategiesByApplicationAssetPair("ripple-euro"))
                .expectNext("threshold")
                .expectComplete()
                .verify();
    }

}
