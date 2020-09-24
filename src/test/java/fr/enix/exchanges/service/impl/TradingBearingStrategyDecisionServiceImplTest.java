package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.service.MarketOfferService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TradingBearingStrategyDecisionServiceImplTest {

    @Autowired private PriceReferenceRepository priceReferenceRepository;
    @Autowired private MarketOfferService marketOfferService;
    @Autowired private TradingBearingStrategyDecisionServiceImpl tradingBearingStrategyDecisionService;

    @Test
    @Order(0)
    void testGetDecisionWhenNoPriceReferenceAndMarketOfferAreSet_shouldReturnDoNothingDecision() {
        StepVerifier
        .create(tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        .consumeNextWith(decision -> assertEquals(Decision.DO_NOTHING, decision))
        .verifyComplete();
    }

    //@Test
    //@Order(1)
    void testGetDecisionWhenPriceReferenceIsSetButMarketOfferIsNotSet_shouldReturnDoNothingDecision() {
        StepVerifier
        .create(
            priceReferenceRepository.updatePriceReferenceForApplicationAssetPair("litecoin-euro", new BigDecimal("50"))
            .flatMap(previousPriceReference -> tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        )
        .consumeNextWith(decision -> assertEquals(Decision.DO_NOTHING, decision))
        .verifyComplete();
    }

    //@Test
    //@Order(2)
    void testGetDecisionWhenPriceReferenceIsNotSetButMarketOfferIsSet_shouldReturnDoNothingDecision() {
        StepVerifier
        .create(
            marketOfferService.saveApplicationAssetPairTicker("litecoin-euro", new BigDecimal("45.5"))
            .flatMap(applicationAssetPair -> tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        )
        .consumeNextWith(decision -> assertEquals(Decision.DO_NOTHING, decision))
        .verifyComplete();
    }

    @Test
    @Order(3)
    void testGetDecisionWhenLastPriceReachHighGap_shouldReturnSellDecision() {
        StepVerifier
        .create(
            marketOfferService.saveApplicationAssetPairTicker(
                    "litecoin-euro", new BigDecimal("39.19")
            )
            .flatMap(applicationAssetPair ->
                priceReferenceRepository.updatePriceReferenceForApplicationAssetPair(
    "litecoin-euro", new BigDecimal("40"))
            )
            .flatMap(newPriceReference ->
                tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        )
        .consumeNextWith(decision -> assertEquals(Decision.SELL, decision))
        .verifyComplete();
    }
}
