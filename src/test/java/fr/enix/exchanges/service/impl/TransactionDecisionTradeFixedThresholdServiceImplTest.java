package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.repository.Threshold;
import fr.enix.exchanges.model.ws.AssetPair;
import fr.enix.exchanges.repository.FixedThresholdRepository;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.TransactionDecisionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class TransactionDecisionTradeFixedThresholdServiceImplTest {

    @Autowired private TransactionDecisionService   transactionDecisionService;
    @Autowired private MarketOfferService           marketOfferService;
    @Autowired private FixedThresholdRepository     fixedThresholdRepository;

    private static final AssetPair ASSET_PAIR = AssetPair.builder   ()
                                                         .from      (Asset.LTC)
                                                         .to        (Asset.EUR)
                                                         .build     ();

    @Test
    @Order(1)
    public void testGetDecisionBasedOnDefaultThresholdValue_shouldReturnDoNothingDecision() {
        StepVerifier.create(marketOfferService.saveNewMarketPrice(ASSET_PAIR, new BigDecimal("39.7"))
                                              .flatMap(marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory)))
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.DO_NOTHING, decision);
                    })
                    .verifyComplete();

    }

    @Test
    @Order(2)
    public void testGetDecisionBasedOnDefaultThresholdValue_shouldReturnBuyDecision() {
        StepVerifier.create(marketOfferService.saveNewMarketPrice(ASSET_PAIR, new BigDecimal("39.65487"))
                                              .flatMap(marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory)))
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.BUY, decision);
                    })
                    .verifyComplete();

    }

    @Test
    @Order(3)
    public void testGetDecisionBasedOnDefaultThresholdValue_shouldReturnSellDecision() {
        StepVerifier.create(marketOfferService.saveNewMarketPrice(ASSET_PAIR, new BigDecimal("39.87652"))
                                              .flatMap(marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory)))
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.SELL, decision);
                    })
                    .verifyComplete();

    }

    private void updateThreshold() {
        fixedThresholdRepository.updateThresholds(Threshold.builder()
                                .thresholdToBuy  (new BigDecimal("39.98515"))
                                .thresholdToSell (new BigDecimal("41.25258"))
                                .build());
    }

    @Test
    @Order(4)
    public void testGetDecisionBasedOnUpdatedThresholdValue_shouldReturnDoNothingDecision() {
        updateThreshold();
        StepVerifier.create(marketOfferService.saveNewMarketPrice(ASSET_PAIR, new BigDecimal("39.99999"))
                                              .flatMap(marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory)))
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.DO_NOTHING, decision);
                    })
                    .verifyComplete();

    }

    @Test
    @Order(5)
    public void testGetDecisionBasedOnUpdatedThresholdValue_shouldReturnBuyDecision() {
        updateThreshold();
        StepVerifier.create(marketOfferService.saveNewMarketPrice(ASSET_PAIR, new BigDecimal("39.98514"))
                                              .flatMap(marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory)))
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.BUY, decision);
                    })
                    .verifyComplete();

    }

    @Test
    @Order(6)
    public void testGetDecisionBasedOnUpdatedThresholdValue_shouldReturnSellDecision() {
        log.info("initial threshold restored: {}", initialThreshold);
        updateThreshold();
        StepVerifier.create(marketOfferService.saveNewMarketPrice(ASSET_PAIR, new BigDecimal("41.25259"))
                .flatMap(marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory)))
                .consumeNextWith(decision -> {
                    assertEquals(Decision.SELL, decision);
                })
                .verifyComplete();

    }


    private static Threshold initialThreshold;

    @Test
    @Order(0)
    public void setUp_saveInitialThreshold() {
        StepVerifier.create(fixedThresholdRepository.getThresholds())
                    .consumeNextWith(threshold -> {
                        TransactionDecisionTradeFixedThresholdServiceImplTest.initialThreshold = threshold;
                        assertNotNull(TransactionDecisionTradeFixedThresholdServiceImplTest.initialThreshold);
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
