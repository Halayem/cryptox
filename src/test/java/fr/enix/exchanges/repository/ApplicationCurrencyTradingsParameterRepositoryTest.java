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
                .create(applicationCurrencyTradingsParameterRepository.getStrategyForApplicationAssetPair("litecoin-euro"))
                .expectNext(ApplicationCurrencyTradingsStrategy.STATIC_BEARING)
                .expectComplete()
                .verify();
    }

    @Test
    void testGetStrategiesByApplicationAssetPair_bitcoinToEuro() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getStrategyForApplicationAssetPair("bitcoin-euro"))
                .expectNext(ApplicationCurrencyTradingsStrategy.STATIC_BEARING)
                .expectComplete()
                .verify();
    }

    @Test
    void testGetStrategiesByApplicationAssetPair_rippleToEuro() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getStrategyForApplicationAssetPair("ripple-euro"))
                .expectNext(ApplicationCurrencyTradingsStrategy.THRESHOLD)
                .expectComplete()
                .verify();
    }

}
