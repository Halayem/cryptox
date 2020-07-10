package fr.enix.exchanges.service;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.ws.AssetPair;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionDecisionServiceTest {

    @Autowired private TransactionDecisionService   transactionDecisionService;
    @Autowired private MarketOfferService           marketOfferService;

    private final AssetPair assetPair = AssetPair.builder().from(Asset.LTC).to(Asset.EUR).build();

    private final BigDecimal firstMarketOfferPrice  = new BigDecimal("44.28");
    private final BigDecimal secondMarketOfferPrice = new BigDecimal("32.25");
    private final BigDecimal thirdMarketOfferPrice  = new BigDecimal("32.24");
    private final BigDecimal fourthMarketOfferPrice = new BigDecimal("32.26");

    @Test
    @Order(0)
    public void setup() {
        marketOfferService.resetAllMarketOfferHistory();
    }

    @Test
    @Order(1)
    public void testGetDecision_shouldReturnDoNothingDecisionWhenHistoricIsNotYetBuilt() {
        StepVerifier.create(marketOfferService.saveNewMarketPrice   (assetPair,firstMarketOfferPrice)
                                              .flatMap              (marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory))
                    )
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.DO_NOTHING, decision);
                    })
                    .verifyComplete();

    }

    @Test
    @Order(2)
    public void testGetDecision_shouldReturnBuyDecisionWhenPriceDrops() {
        StepVerifier.create(marketOfferService.saveNewMarketPrice   (assetPair,secondMarketOfferPrice)
                                              .flatMap              (marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory))
                    )
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.BUY, decision);
                    })
                    .verifyComplete();
    }

    @Test
    @Order(3)
    public void testGetDecision_shouldReturnBuyDecisionWhenPriceContinuesToDrop() {
        StepVerifier.create(marketOfferService.saveNewMarketPrice   (assetPair,thirdMarketOfferPrice)
                                              .flatMap              (marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory))
                    )
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.BUY, decision);
                    })
                    .verifyComplete();
    }

    @Test
    @Order(4)
    public void testGetDecision_shouldReturnSellDecisionWhenPriceGoesUp() {
        StepVerifier.create(marketOfferService.saveNewMarketPrice   (assetPair,fourthMarketOfferPrice)
                                              .flatMap              (marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory))
                    )
                    .consumeNextWith(decision -> {
                        assertEquals(Decision.SELL, decision);
                    })
                    .verifyComplete();
    }

    @Test
    @Order(5)
    public void tearDown() {
        marketOfferService.resetAllMarketOfferHistory();
    }

}
