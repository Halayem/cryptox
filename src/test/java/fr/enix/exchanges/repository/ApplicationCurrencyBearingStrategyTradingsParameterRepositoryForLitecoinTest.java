package fr.enix.exchanges.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationCurrencyBearingStrategyTradingsParameterRepositoryForLitecoinTest {


    @Autowired
    private ApplicationBearingStrategyParameterRepository applicationBearingStrategyParameterRepository;
    private final String applicationAssetPairToTest = "litecoin-euro";

    @Test
    void testGetGap() {
        StepVerifier
                .create(applicationBearingStrategyParameterRepository.getGapByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(gap -> {
                    assertEquals(new BigDecimal("0.8"), gap);
                })
                .verifyComplete();
    }

    @Test
    void testGetAmountToSell() {
        StepVerifier
                .create(applicationBearingStrategyParameterRepository.getAmountToSellByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(amountToSell -> {
                    assertEquals(new BigDecimal("0.5"), amountToSell);
                })
                .verifyComplete();
    }

    @Test
    void testGetAmountToBuy() {
        StepVerifier
                .create(applicationBearingStrategyParameterRepository.getAmountToBuyByApplicationAssetPair(applicationAssetPairToTest))
                .consumeNextWith(amountToBuy -> {
                    assertEquals(new BigDecimal("0.5"), amountToBuy);
                })
                .verifyComplete();
    }

}
