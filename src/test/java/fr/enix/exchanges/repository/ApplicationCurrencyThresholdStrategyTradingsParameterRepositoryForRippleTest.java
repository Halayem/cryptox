package fr.enix.exchanges.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ApplicationCurrencyThresholdStrategyTradingsParameterRepositoryForRippleTest {

    @Autowired
    private ApplicationThresholdStrategyParameterRepository applicationThresholdStrategyParameterRepository;
    private final String applicationAssetPairToTest = "ripple-euro";

    @Test
    void testGetTriggerPriceToBuy() {
        StepVerifier
                .create(applicationThresholdStrategyParameterRepository.getTriggerPriceToBuyByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(triggerPriceToBuy -> {
                    assertEquals(new BigDecimal("0.20122"), triggerPriceToBuy);
                })
                .verifyComplete();
    }

    @Test
    void testGetTriggerAmountToBuy() {
        StepVerifier
                .create(applicationThresholdStrategyParameterRepository.getAmountToBuyByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(amountToBuy -> {
                    assertEquals(new BigDecimal("445"), amountToBuy);
                })
                .verifyComplete();
    }

    @Test
    void testGetTriggerPriceToSell() {
        StepVerifier
                .create(applicationThresholdStrategyParameterRepository.getTriggerPriceToSellByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(triggerPriceToSell -> {
                    assertEquals(new BigDecimal("0.21242"), triggerPriceToSell);
                })
                .verifyComplete();
    }

    @Test
    void testGetTriggerAmountToSell() {
        StepVerifier
                .create(applicationThresholdStrategyParameterRepository.getAmountToSellByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(amountToSell -> {
                    assertEquals(new BigDecimal("285"), amountToSell);
                })
                .verifyComplete();
    }
}
