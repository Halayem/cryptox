package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.repository.PriceReferenceRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceReferenceRepositoryImplTest {

    @Autowired
    private PriceReferenceRepository priceReferenceRepository;

    @Test
    @Order(0)
    void testGetPriceReferenceForApplicationAssetPair_shouldReturnEmptyMonoWhenNoPriceReferenceExist() {
        StepVerifier
        .create(priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro"))
        .verifyComplete();
    }

    @Test
    @Order(1)
    void testUpdatePriceReferenceForApplicationAssetPair_shouldReturnUpdatedPriceReference() {
        StepVerifier
        .create(priceReferenceRepository.updatePriceReferenceForApplicationAssetPair("litecoin-euro", new BigDecimal("42.15"), "application"))
        .consumeNextWith(newPriceReference -> {
            assertEquals(new BigDecimal("42.15"), newPriceReference.getPrice());
            assertEquals("application", newPriceReference.getUpdatedBy());
            assertTrue(LocalDateTime.now().isAfter(newPriceReference.getDatetime()));
        })
        .verifyComplete();
    }

    @Test
    @Order(2)
    void testGetPriceReferenceForApplicationAssetPair_shouldReturnCurrentPriceReference() {
        StepVerifier
        .create(priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro"))
        .consumeNextWith(priceReference -> {
            assertEquals(new BigDecimal("42.15"), priceReference.getPrice()     );
            assertTrue  (LocalDateTime.now().isAfter(priceReference.getDatetime())  );
        })
        .verifyComplete();
    }

    @Test
    @Order(3)
    void testSecondUpdatePriceReferenceForApplicationAssetPair_shouldReturnPreviousPriceReference() {
        StepVerifier
        .create(priceReferenceRepository.updatePriceReferenceForApplicationAssetPair("litecoin-euro", new BigDecimal("56.554"), "user"))
        .consumeNextWith(previousPriceReference -> {
            assertEquals(new BigDecimal("56.554"), previousPriceReference.getPrice()    );
            assertEquals("user", previousPriceReference.getUpdatedBy());
            assertTrue  (LocalDateTime.now().isAfter(previousPriceReference.getDatetime())  );
        })
        .verifyComplete();
    }
}
