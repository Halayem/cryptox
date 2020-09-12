package fr.enix.exchanges.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationCurrencyTradingsParameterRepositoryTest {

    @Autowired
    private ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    @Test
    public void testGetEnabledCurrenciesForTrading() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getEnabledCurrenciesForTrading())
                .expectNext("litecoin-euro")
                .expectNext("bitcoin-euro")
                .expectNext("ripple-euro")
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetStrategiesByApplicationAssetPair_litecoinToEuro() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getStrategiesByApplicationAssetPair("litecoin-euro"))
                .expectNext("bearing")
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetStrategiesByApplicationAssetPair_bitcoinToEuro() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getStrategiesByApplicationAssetPair("bitcoin-euro"))
                .expectNext("bearing")
                .expectNext("threshold")
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetStrategiesByApplicationAssetPair_rippleToEuro() {
        StepVerifier
                .create(applicationCurrencyTradingsParameterRepository.getStrategiesByApplicationAssetPair("ripple-euro"))
                .expectNext("threshold")
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetPriceGapByApplicationAssetPair_litecoinToEuro() {
        StepVerifier.create(applicationCurrencyTradingsParameterRepository.getPriceGapByApplicationAssetPair("litecoin-euro"))
                .consumeNextWith(priceGap -> {
                    assertEquals    (new BigDecimal("0.8"), priceGap);
                })
                .verifyComplete();
    }

    @Test
    public void testGetPriceGapByApplicationAssetPair_bitcoinToEuro() {
        StepVerifier.create(applicationCurrencyTradingsParameterRepository.getPriceGapByApplicationAssetPair("bitcoin-euro"))
                .consumeNextWith(priceGap -> {
                    assertEquals    (new BigDecimal("0.00001"), priceGap);
                })
                .verifyComplete();
    }

    @Test
    public void testGetPriceGapByApplicationAssetPair_rippleToEuro() {
        StepVerifier.create(applicationCurrencyTradingsParameterRepository.getPriceGapByApplicationAssetPair("ripple-euro"))
                .consumeNextWith(priceGap -> {
                    assertEquals    (new BigDecimal("2.452"), priceGap);
                })
                .verifyComplete();
    }
}
