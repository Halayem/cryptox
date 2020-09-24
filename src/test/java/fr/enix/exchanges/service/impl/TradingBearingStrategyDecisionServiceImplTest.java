package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.repository.PriceReference;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.service.MarketOfferService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;



import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TradingBearingStrategyDecisionServiceImplTest {

    @MockBean private PriceReferenceRepository priceReferenceRepository;
    @MockBean private MarketOfferService marketOfferService;

    @Autowired private TradingBearingStrategyDecisionServiceImpl tradingBearingStrategyDecisionService;

    @Test
    @Order(0)
    void testGetDecisionWhenNoPriceReferenceAndMarketOfferAreSet_shouldReturnDoNothingDecision() {
        Mockito
        .when       (priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.empty());

        Mockito
        .when       (marketOfferService.getLastPriceByApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.empty());


        StepVerifier
        .create(tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        .consumeNextWith(decision -> assertEquals(Decision.DO_NOTHING, decision))
        .verifyComplete();
    }

    @Test
    @Order(1)
    void testGetDecisionWhenPriceReferenceIsSetButMarketOfferIsNotSet_shouldReturnDoNothingDecision() {

        Mockito
        .when       (priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro") )
        .thenReturn (Mono.just(PriceReference.builder().price(new BigDecimal("456.666")).build())   );

        Mockito
        .when       (marketOfferService.getLastPriceByApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.empty());

        StepVerifier
        .create         (tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        .consumeNextWith(decision -> assertEquals(Decision.DO_NOTHING, decision))
        .verifyComplete ();
    }

    @Test
    @Order(2)
    void testGetDecisionWhenPriceReferenceIsNotSetButMarketOfferIsSet_shouldReturnDoNothingDecision() {
        Mockito
        .when       (priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro") )
        .thenReturn (Mono.empty()   );

        Mockito
        .when       (marketOfferService.getLastPriceByApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.just(new BigDecimal(("45"))));

        StepVerifier
        .create         (tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        .consumeNextWith(decision -> assertEquals(Decision.DO_NOTHING, decision))
        .verifyComplete ();
    }

    @Test
    @Order(3)
    void testGetDecisionWhenLastPriceReachHighGap_shouldReturnSellDecision() {
        // gap of 0.8 is set in application properties
        Mockito
        .when       (priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro") )
        .thenReturn (
            Mono.just(
                PriceReference
                .builder()
                .price  (new BigDecimal("45.65"))
                .build  ()
            )
        );

        Mockito
        .when       (marketOfferService.getLastPriceByApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.just(new BigDecimal(("46.45"))));

        StepVerifier
        .create         (tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        .consumeNextWith(decision -> assertEquals(Decision.SELL, decision))
        .verifyComplete ();
    }

    @Test
    @Order(4)
    void testGetDecisionWhenLastPriceReachLowGap_shouldReturnBuyDecision() {
        // gap of 0.8 is set in application properties
        Mockito
        .when       (priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro") )
        .thenReturn (
                Mono.just(
                    PriceReference
                    .builder()
                    .price  (new BigDecimal("45.65"))
                    .build  ()
                )
        );

        Mockito
        .when       (marketOfferService.getLastPriceByApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.just(new BigDecimal(("44.85"))));

        StepVerifier
        .create         (tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        .consumeNextWith(decision -> assertEquals(Decision.BUY, decision))
        .verifyComplete ();
    }

    @Test
    @Order(5)
    void testGetDecisionWhenLastPriceIsBetweenLowAndHighGap_shouldReturnDoNothingDecision() {
        // gap of 0.8 is set in application properties
        Mockito
        .when       (priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro") )
        .thenReturn (
                Mono.just(
                    PriceReference
                    .builder()
                    .price  (new BigDecimal("45.65"))
                    .build  ()
                )
        );

        Mockito
        .when       (marketOfferService.getLastPriceByApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.just(new BigDecimal(("44.95"))));

        StepVerifier
        .create         (tradingBearingStrategyDecisionService.getDecision("litecoin-euro"))
        .consumeNextWith(decision -> assertEquals(Decision.DO_NOTHING, decision))
        .verifyComplete ();
    }
}
