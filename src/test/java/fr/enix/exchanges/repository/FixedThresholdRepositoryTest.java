package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.Threshold;
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
public class FixedThresholdRepositoryTest {

    @Autowired private FixedThresholdRepository fixedThresholdRepository;

    @Test
    @Order(0)
    public void testDefaultThresholdValueDefinedOnApplicationStartup() {
        StepVerifier.create(fixedThresholdRepository.getThresholds())
                    .consumeNextWith(threshold -> {
                        assertEquals(new BigDecimal("39.65487"), threshold.getThresholdToBuy());
                        assertEquals(new BigDecimal("39.87652"), threshold.getThresholdToSell());
                    })
                    .verifyComplete();
    }

    @Test
    @Order(1)
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
}
