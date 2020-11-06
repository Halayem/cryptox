package fr.enix.exchanges.strategy.bearing;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AmountEnhancerServiceTest {

    @Autowired
    AmountEnhancerService amountEnhancerService;

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
}
