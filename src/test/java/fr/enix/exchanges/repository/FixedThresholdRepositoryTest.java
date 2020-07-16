package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.Threshold;
import fr.enix.exchanges.service.impl.TransactionDecisionTradeFixedThresholdServiceImplTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class FixedThresholdRepositoryTest {

    @Autowired private FixedThresholdRepository fixedThresholdRepository;

    @Test
    @Order(1)
    public void testDefaultThresholdValueDefinedOnApplicationStartup() {
        StepVerifier.create(fixedThresholdRepository.getThresholds())
                    .consumeNextWith(threshold -> {
                        assertEquals(new BigDecimal("39.65487"), threshold.getThresholdToBuy());
                        assertEquals(new BigDecimal("39.87652"), threshold.getThresholdToSell());
                    })
                    .verifyComplete();
    }

    @Test
    @Order(2)
    public void testUpdateThresholds() {
        fixedThresholdRepository.updateThresholds(
                Threshold.builder()
                         .thresholdToBuy(new BigDecimal("37.62154"))
                         .thresholdToSell(new BigDecimal("38.68892"))
                         .build()
        );
        StepVerifier.create(fixedThresholdRepository.getThresholds())
                    .consumeNextWith(threshold -> {
                        assertEquals(new BigDecimal("37.62154"), threshold.getThresholdToBuy());
                        assertEquals(new BigDecimal("38.68892"), threshold.getThresholdToSell());
                    })
                    .verifyComplete();
    }

    private static Threshold initialThreshold;

    @Test
    @Order(0)
    public void setUp_saveInitialThreshold() {
        StepVerifier.create(fixedThresholdRepository.getThresholds())
                .consumeNextWith(threshold -> {
                    FixedThresholdRepositoryTest.initialThreshold = threshold;
                    assertNotNull(FixedThresholdRepositoryTest.initialThreshold);
                })
                .verifyComplete();

    }

    @Test
    @Order(999)
    public void tearDown_restoreInitialThreshold() {
        fixedThresholdRepository.updateThresholds(initialThreshold);
        log.info("initial threshold restored: {}", initialThreshold);
    }
}
