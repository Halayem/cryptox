package fr.enix.exchanges.strategy.bearing.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class AmountEnhancerServiceTest {

    @Autowired
    AmountEnhancerServiceImpl amountEnhancerService;

    @Test
    @Order(0)
    void testGetNewAmountEnhanceForSell_shouldReturnZeroForFirstSellRequest() {
        assertEquals( BigDecimal.ZERO, amountEnhancerService.getNewAmountEnhanceForSell("litecoin-euro"));
    }

    @Test
    @Order(1)
    void testGetNewAmountEnhanceForSell_shouldBeTheConfiguredStepForSecondConsecutiveSellRequest() {
        assertEquals( new BigDecimal("0.2"), amountEnhancerService.getNewAmountEnhanceForSell("litecoin-euro"));
    }

    @Test
    @Order(2)
    void testGetNewAmountEnhanceForSell_shouldBeTheDoubleOfConfiguredStepForThirdConsecutiveSellRequest() {
        assertEquals( new BigDecimal("0.4"), amountEnhancerService.getNewAmountEnhanceForSell("litecoin-euro"));
    }

    @Test
    @Order(3)
    void testGetNewAmountEnhanceForBuy_shouldReturnZeroForFirstBuyRequestComingAfterOneOrMultipleSellRequests() {
        assertEquals( BigDecimal.ZERO, amountEnhancerService.getNewAmountEnhanceForBuy("litecoin-euro"));
    }

    @Test
    @Order(4)
    void testGetNewAmountEnhanceForBuy_shouldBeTheConfiguredStepForSecondConsecutiveBuyRequest() {
        assertEquals( new BigDecimal("0.2"), amountEnhancerService.getNewAmountEnhanceForBuy("litecoin-euro"));
    }

    @Test
    @Order(5)
    void testGetNewAmountEnhanceForBuy_shouldBeTheDoubleOfConfiguredStepForThirdConsecutiveBuyRequest() {
        assertEquals( new BigDecimal("0.4"), amountEnhancerService.getNewAmountEnhanceForBuy("litecoin-euro"));
    }

    @Test
    @Order(6)
    void testGetNewAmountEnhanceForSell_shouldReturnZeroForFirstSellRequestComingAfterOneOrMultipleBuyRequests() {
        assertEquals( BigDecimal.ZERO, amountEnhancerService.getNewAmountEnhanceForSell("litecoin-euro"));
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
