package fr.enix.exchanges.strategy.bearing.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class AmountEnhancerServiceTest {

    @Autowired
    private AmountEnhancerServiceImpl amountEnhancerService;

    private final String applicationAssetPair = "bitcoin-euro";

    @Test
    @Order(0)
    void testGetNewAmountEnhanceForSell_shouldReturnZeroForFirstSellRequest() {
        StepVerifier
        .create         ( amountEnhancerService.getNewAmountEnhanceForSell( applicationAssetPair ) )
        .consumeNextWith( newAmountEnhanceForSell -> assertEquals( BigDecimal.ZERO, newAmountEnhanceForSell ) )
        .verifyComplete ();

    }

    @Test
    @Order(1)
    void testGetNewAmountEnhanceForSell_shouldBeTheConfiguredStepForSecondConsecutiveSellRequest() {
        StepVerifier
        .create         ( amountEnhancerService.getNewAmountEnhanceForSell( applicationAssetPair ) )
        .consumeNextWith( newAmountEnhanceForSell -> assertEquals( new BigDecimal("0.00000001"), newAmountEnhanceForSell ) )
        .verifyComplete ();
    }

    @Test
    @Order(2)
    void testGetNewAmountEnhanceForSell_shouldBeTheDoubleOfConfiguredStepForThirdConsecutiveSellRequest() {
        StepVerifier
        .create         ( amountEnhancerService.getNewAmountEnhanceForSell( applicationAssetPair ) )
        .consumeNextWith( newAmountEnhanceForSell -> assertEquals( new BigDecimal("0.00000002"), newAmountEnhanceForSell ) )
        .verifyComplete ();
    }

    @Test
    @Order(3)
    void testGetNewAmountEnhanceForBuy_shouldReturnZeroForFirstBuyRequestComingAfterOneOrMultipleSellRequests() {
        StepVerifier
        .create         ( amountEnhancerService.getNewAmountEnhanceForBuy( applicationAssetPair ) )
        .consumeNextWith( newAmountEnhanceForBuy -> assertEquals( BigDecimal.ZERO, newAmountEnhanceForBuy ) )
        .verifyComplete ();
    }

    @Test
    @Order(4)
    void testGetNewAmountEnhanceForBuy_shouldBeTheConfiguredStepForSecondConsecutiveBuyRequest() {
        StepVerifier
        .create         ( amountEnhancerService.getNewAmountEnhanceForBuy( applicationAssetPair ) )
        .consumeNextWith( newAmountEnhanceForBuy -> assertEquals( new BigDecimal("0.00000001"), newAmountEnhanceForBuy ) )
        .verifyComplete ();
    }

    @Test
    @Order(5)
    void testGetNewAmountEnhanceForBuy_shouldBeTheDoubleOfConfiguredStepForThirdConsecutiveBuyRequest() {
        StepVerifier
        .create         ( amountEnhancerService.getNewAmountEnhanceForBuy( applicationAssetPair ) )
        .consumeNextWith( newAmountEnhanceForBuy -> assertEquals( new BigDecimal("0.00000002"), newAmountEnhanceForBuy ) )
        .verifyComplete ();
    }

    @Test
    @Order(6)
    void testGetNewAmountEnhanceForSell_shouldReturnZeroForFirstSellRequestComingAfterOneOrMultipleBuyRequests() {
        StepVerifier
        .create         ( amountEnhancerService.getNewAmountEnhanceForSell( applicationAssetPair ) )
        .consumeNextWith( newAmountEnhanceForSell -> assertEquals( BigDecimal.ZERO, newAmountEnhanceForSell ) )
        .verifyComplete ();
    }

    @BeforeAll
    public void setup() {
        amountEnhancerService.resetAllComputedAmountEnhance();
        log.info("send request to reset all computed amount enhance before executing unit tests");
    }

    @AfterAll
    public void tearDown() {
        amountEnhancerService.resetAllComputedAmountEnhance();
        log.info("send request to reset all computed amount enhance after executing unit tests");
    }
}
