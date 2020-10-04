package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.model.repository.PriceReference;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.service.ExchangeService;
import org.junit.jupiter.api.MethodOrderer;
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

import static fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision.Decision;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TradingBearingStrategyDecisionServiceImplTest {

    @MockBean private PriceReferenceRepository  priceReferenceRepository;
    @MockBean private ExchangeService           exchangeService;

    @Autowired private TradingBearingStrategyDecisionServiceImpl tradingBearingStrategyDecisionService;

    @Test
    void testGetDecisionWhenPriceReferenceIsNotSet_shouldReturnDoNothingDecision() {
        Mockito
        .when       (priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.empty());

        StepVerifier
        .create(tradingBearingStrategyDecisionService.getDecision(
            ApplicationAssetPairTicker
            .builder()
            .applicationAssetPair ( "litecoin-euro" )
            .price                ( new BigDecimal("45.65") )
            .build())
        )
        .consumeNextWith(applicationAssetPairTickerTradingDecision ->
            assertEquals(Decision.ERROR, applicationAssetPairTickerTradingDecision.getDecision()))
        .verifyComplete();
    }

    @Test
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

        StepVerifier
        .create(tradingBearingStrategyDecisionService.getDecision(
            ApplicationAssetPairTicker
            .builder()
            .applicationAssetPair ( "litecoin-euro" )
            .price                ( new BigDecimal("46.45") )
            .build())
        ).consumeNextWith(applicationAssetPairTickerTradingDecision ->
            assertEquals(Decision.SELL, applicationAssetPairTickerTradingDecision.getDecision()))
        .verifyComplete ();
    }

    @Test
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

        StepVerifier
        .create(tradingBearingStrategyDecisionService.getDecision(
            ApplicationAssetPairTicker
            .builder()
            .applicationAssetPair ( "litecoin-euro" )
            .price                ( new BigDecimal("44.85") )
            .build())
        ).consumeNextWith(applicationAssetPairTickerTradingDecision ->
            assertEquals(Decision.BUY, applicationAssetPairTickerTradingDecision.getDecision()))
        .verifyComplete ();
    }

    @Test
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

        StepVerifier
        .create(tradingBearingStrategyDecisionService.getDecision(
            ApplicationAssetPairTicker
            .builder()
            .applicationAssetPair ( "litecoin-euro" )
            .price                ( new BigDecimal("44.95") )
            .build())
        ).consumeNextWith(applicationAssetPairTickerTradingDecision ->
            assertEquals(Decision.DO_NOTHING, applicationAssetPairTickerTradingDecision.getDecision()))
        .verifyComplete ();
    }

    @Test
    void testGetAmountToSell_shouldReturnTheConfiguredValueWhenAvailableAssetIsGreaterThanTheConfiguredValue() {
        Mockito
        .when       (exchangeService.getAvailableAssetForSellPlacementByApplicationAssetPair("litecoin-euro") )
        .thenReturn ( Mono.just(new BigDecimal("0.8") ) );

        StepVerifier
        .create         (tradingBearingStrategyDecisionService.getAmountToSell("litecoin-euro"))
        .consumeNextWith(amountToSell ->
            assertEquals( new BigDecimal("0.5"), amountToSell )
        )
        .verifyComplete();
    }

    @Test
    void testGetAmountToSell_shouldReturnTheAvailableAssetWhenAvailableAssetIsLessThanTheConfiguredValue() {
        Mockito
        .when       (exchangeService.getAvailableAssetForSellPlacementByApplicationAssetPair("litecoin-euro") )
        .thenReturn ( Mono.just(new BigDecimal("0.2") ) );

        StepVerifier
        .create         (tradingBearingStrategyDecisionService.getAmountToSell("litecoin-euro"))
        .consumeNextWith(amountToSell ->
            assertEquals( new BigDecimal("0.2"), amountToSell )
        )
        .verifyComplete();
    }

    @Test
    void testGetAmountToBuy_shouldReturnTheConfiguredValueWhenConfiguredValueMultipliedByMarketPriceIsLessThanAvailableAsset() {
        // amount to buy in litecoin is 0.5 in application properties
        Mockito
        .when       (exchangeService.getAvailableAssetForBuyPlacementByApplicationAssetPair("litecoin-euro") )
        .thenReturn ( Mono.just(new BigDecimal("40") ) );

        StepVerifier
        .create( tradingBearingStrategyDecisionService.getAmountToBuy(
                        ApplicationAssetPairTicker
                        .builder()
                        .applicationAssetPair("litecoin-euro")
                        .price(new BigDecimal("45.245"))
                        .build()
                    )
        )
        .consumeNextWith(amountToBuy ->
            assertEquals( new BigDecimal("0.5"), amountToBuy )
        )
        .verifyComplete();
    }

    @Test
    void testGetAmountToBuy_shouldReturnTheDividedAvailableAssetForBuyByMarketPriceWhenAvailableAssetCanNotBuyTheConfiguredAmountToBuy() {
        // amount to buy in litecoin is 0.5 in application properties
        Mockito
        .when       (exchangeService.getAvailableAssetForBuyPlacementByApplicationAssetPair("litecoin-euro") )
        .thenReturn ( Mono.just(new BigDecimal("17.25") ) );

        StepVerifier
        .create( tradingBearingStrategyDecisionService.getAmountToBuy(
                    ApplicationAssetPairTicker
                        .builder()
                        .applicationAssetPair("litecoin-euro")
                        .price(new BigDecimal("45.245"))
                        .build()
                )
        )
        .consumeNextWith(amountToBuy ->
            assertEquals( new BigDecimal("0.38125759"), amountToBuy )
        )
        .verifyComplete();
    }

}
