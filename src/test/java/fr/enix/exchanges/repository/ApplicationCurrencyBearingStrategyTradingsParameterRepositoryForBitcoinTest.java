package fr.enix.exchanges.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationCurrencyBearingStrategyTradingsParameterRepositoryForBitcoinTest {


    @Autowired
    private ApplicationBearingStrategyParameterRepository applicationBearingStrategyParameterRepository;
    private final String applicationAssetPairToTest = "bitcoin-euro";

    @Test
    public void testGetGap() {
        StepVerifier
                .create(applicationBearingStrategyParameterRepository.getGapByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(gap -> {
                    assertEquals(new BigDecimal("0.0001"), gap);
                })
                .verifyComplete();
    }

    @Test
    public void testGetAmountToSell() {
        StepVerifier
                .create(applicationBearingStrategyParameterRepository.getAmountToSellByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(amountToSell -> {
                    assertEquals(new BigDecimal("0.0004"), amountToSell);
                })
                .verifyComplete();
    }

    @Test
    public void testGetAmountToBuy() {
        StepVerifier
                .create(applicationBearingStrategyParameterRepository.getAmountToBuyByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(amountToBuy -> {
                    assertEquals(new BigDecimal("0.00021"), amountToBuy);
                })
                .verifyComplete();
    }

}
