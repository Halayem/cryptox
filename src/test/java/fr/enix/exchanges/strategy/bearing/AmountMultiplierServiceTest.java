package fr.enix.exchanges.strategy.bearing;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AmountMultiplierServiceTest {

    @Autowired AmountMultiplierService amountMultiplierService;

    @Test
    @Order(0)
    void testGetNewAmountMultiplierForSell_shouldBeOneForFirstSellRequest() {
        assertEquals(1, amountMultiplierService.getNewAmountMultiplierForSell("litecoin-euro"));
       
    }

    @Test
    @Order(1)
    void testGetNewAmountMultiplierForSell_shouldBeTwoForSecondConsecutiveSellRequest() {
        assertEquals(2, amountMultiplierService.getNewAmountMultiplierForSell("litecoin-euro"));
    }

    @Test
    @Order(2)
    void testGetNewAmountMultiplierForSell_shouldBeThreeForThirdConsecutiveSellRequest() {
        assertEquals(3, amountMultiplierService.getNewAmountMultiplierForSell("litecoin-euro"));
    }

    @Test
    @Order(3)
    void testGetNewAmountMultiplierForBuy_shouldBeOneForFirstBuyRequest() {
        assertEquals(1, amountMultiplierService.getNewAmountMultiplierForBuy("litecoin-euro"));
    }

    @Test
    @Order(4)
    void testGetNewAmountMultiplierForBuy_shouldBeTwoForSecondConsecutiveBuyRequest() {
        assertEquals(2, amountMultiplierService.getNewAmountMultiplierForBuy("litecoin-euro"));
    }

    @Test
    @Order(5)
    void testGetNewAmountMultiplierForBuy_shouldBeThreeForThirdConsecutiveBuyRequest() {
        assertEquals(3, amountMultiplierService.getNewAmountMultiplierForBuy("litecoin-euro"));
    }

    @Test
    @Order(6)
    void testGetNewAmountMultiplierForSell_shouldBeOneAfterConsecutiveBuyRequest() {
        assertEquals(1, amountMultiplierService.getNewAmountMultiplierForSell("litecoin-euro"));
    }
}
