package fr.enix.exchanges.strategy.bearing;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AmountMultiplierServiceTest {

    @Autowired AmountMultiplierService amountMultiplierService;

    @Test
    @Order(0)
    void testGetAmountMultiplierForSell_shouldBeOneForFirstSellRequest() {
        StepVerifier
        .create         (amountMultiplierService.getAmountMultiplierForSell("litecoin-euro"))
        .consumeNextWith(amountMultiplier -> assertEquals(1, amountMultiplier ))
        .verifyComplete ();
    }

    @Test
    @Order(1)
    void testGetAmountMultiplierForSell_shouldBeTwoForSecondConsecutiveSellRequest() {
        StepVerifier
        .create         (amountMultiplierService.getAmountMultiplierForSell("litecoin-euro"))
        .consumeNextWith(amountMultiplier -> assertEquals(2, amountMultiplier ))
        .verifyComplete ();
    }

    @Test
    @Order(2)
    void testGetAmountMultiplierForSell_shouldBeThreeForThirdConsecutiveSellRequest() {
        StepVerifier
        .create         (amountMultiplierService.getAmountMultiplierForSell("litecoin-euro"))
        .consumeNextWith(amountMultiplier -> assertEquals(3, amountMultiplier ))
        .verifyComplete ();
    }

    @Test
    @Order(3)
    void testGetAmountMultiplierForBuy_shouldBeOneForFirstBuyRequest() {
        StepVerifier
        .create         (amountMultiplierService.getAmountMultiplierForBuy("litecoin-euro"))
        .consumeNextWith(amountMultiplier -> assertEquals(1, amountMultiplier ))
        .verifyComplete ();
    }

    @Test
    @Order(4)
    void testGetAmountMultiplierForBuy_shouldBeTwoForSecondConsecutiveBuyRequest() {
        StepVerifier
        .create         (amountMultiplierService.getAmountMultiplierForBuy("litecoin-euro"))
        .consumeNextWith(amountMultiplier -> assertEquals(2, amountMultiplier ))
        .verifyComplete ();
    }

    @Test
    @Order(5)
    void testGetAmountMultiplierForBuy_shouldBeThreeForThirdConsecutiveBuyRequest() {
        StepVerifier
        .create         (amountMultiplierService.getAmountMultiplierForBuy("litecoin-euro"))
        .consumeNextWith(amountMultiplier -> assertEquals(3, amountMultiplier ))
        .verifyComplete ();
    }

    @Test
    @Order(6)
    void testGetAmountMultiplierForSell_shouldBeOneAfterConsecutiveBuyRequest() {
        StepVerifier
        .create         (amountMultiplierService.getAmountMultiplierForSell("litecoin-euro"))
        .consumeNextWith(amountMultiplier -> assertEquals(1, amountMultiplier ))
        .verifyComplete ();
    }
}
