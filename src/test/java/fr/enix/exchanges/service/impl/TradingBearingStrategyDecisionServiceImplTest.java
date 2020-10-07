package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.model.repository.PriceReference;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.service.ExchangeService;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@RunWith(SpringRunner.class)
class TradingBearingStrategyDecisionServiceImplTest {

    @MockBean private PriceReferenceRepository  priceReferenceRepository;
    @MockBean private ExchangeService           exchangeService;

    @Autowired private TradingBearingStrategyDecisionServiceImpl tradingDecisionService;

    @Test
    void testGetDecisionWhenPriceReferenceIsNotSet_shouldReturnErrorDecision() {
        Mockito
        .when       (priceReferenceRepository.getPriceReferenceForApplicationAssetPair("litecoin-euro"))
        .thenReturn (Mono.empty());

        StepVerifier
        .create(tradingDecisionService.getDecision(
            ApplicationAssetPairTicker
            .builder()
            .applicationAssetPair ( "litecoin-euro" )
            .price                ( new BigDecimal("45.65") )
            .build())
        )
        .consumeNextWith(applicationAssetPairTickerTradingDecision -> {
            assertEquals(Decision.ERROR, applicationAssetPairTickerTradingDecision.getOperation().getDecision());
            assertEquals(   "price reference was not set for this application asset pair: <litecoin-euro>",
                            applicationAssetPairTickerTradingDecision.getOperation().getMessage()
            );
            assertNull(applicationAssetPairTickerTradingDecision.getAmount());
            assertNull(applicationAssetPairTickerTradingDecision.getPrice());
        })
        .verifyComplete();
    }


    @Test
    void testGetDecisionWhenLastPriceReachHighGap_shouldReturnSellDecision() {
        setupPriceReferenceRepositoryMockForLitecoinEuro();

        final TradingBearingStrategyDecisionServiceImpl tradingDecisionServiceSpy = Mockito.spy(tradingDecisionService);
        Mockito
        .doReturn   ( Mono.just( new BigDecimal("0.5" ) ) )
        .when       ( tradingDecisionServiceSpy ).getAmountToSell( "litecoin-euro" );

        StepVerifier
        .create         ( tradingDecisionServiceSpy.getDecision( newApplicationAssetPairTickerForLitecoinEuro( new BigDecimal("46.45") )))
        .consumeNextWith( applicationAssetPairTickerTradingDecision -> {
            assertEquals( Decision.SELL, applicationAssetPairTickerTradingDecision.getOperation().getDecision() );
            assertEquals( new BigDecimal("0.5" ), applicationAssetPairTickerTradingDecision.getAmount() );
            assertEquals( new BigDecimal("46.45"), applicationAssetPairTickerTradingDecision.getPrice() );
        })
        .verifyComplete ();
    }

    @Test
    void testGetDecisionWhenLastPriceReachHighGapButAvailableAssetIsLessThanTheMinimumOrder_shouldReturnDoNothingDecision() {
        setupPriceReferenceRepositoryMockForLitecoinEuro();

        final TradingBearingStrategyDecisionServiceImpl tradingDecisionServiceSpy = Mockito.spy(tradingDecisionService);
        Mockito
        .doReturn   ( Mono.just( new BigDecimal("0" ) ) )
        .when       ( tradingDecisionServiceSpy ).getAmountToSell( "litecoin-euro" );

        StepVerifier
        .create         ( tradingDecisionServiceSpy.getDecision( newApplicationAssetPairTickerForLitecoinEuro( new BigDecimal("46.45") ) ) )
        .consumeNextWith( applicationAssetPairTickerTradingDecision -> {
            assertEquals( Decision.DO_NOTHING, applicationAssetPairTickerTradingDecision.getOperation().getDecision() );
            assertEquals(
                "the computed amount to sell: <0,000000>, is less than the minimum order: <0,100000>",
                applicationAssetPairTickerTradingDecision.getOperation().getMessage()
            );

            assertNull( applicationAssetPairTickerTradingDecision.getAmount()   );
            assertNull( applicationAssetPairTickerTradingDecision.getPrice()    );
        })
        .verifyComplete ();
    }


    @Test
    void testGetDecisionWhenLastPriceReachLowGap_shouldReturnBuyDecision() {
        setupPriceReferenceRepositoryMockForLitecoinEuro();

        final TradingBearingStrategyDecisionServiceImpl tradingDecisionServiceSpy = Mockito.spy(tradingDecisionService);
        Mockito
        .doReturn   ( Mono.just( new BigDecimal("0.5" ) ) )
        .when       ( tradingDecisionServiceSpy ).getAmountToBuy( Mockito.any(ApplicationAssetPairTicker.class) );

        StepVerifier
        .create         ( tradingDecisionServiceSpy.getDecision(newApplicationAssetPairTickerForLitecoinEuro( new BigDecimal("44.850000") )))
        .consumeNextWith( applicationAssetPairTickerTradingDecision -> {
            assertEquals( Decision.BUY, applicationAssetPairTickerTradingDecision.getOperation().getDecision() );
            assertEquals( new BigDecimal("0.5" ), applicationAssetPairTickerTradingDecision.getAmount() );
            assertEquals( new BigDecimal("44.850000"), applicationAssetPairTickerTradingDecision.getPrice() );
        })
        .verifyComplete ();
    }

    @Test
    void testGetDecisionWhenLastPriceReachLowGapButAvailableAssetIsLessThanTheMinimumOrder_shouldReturnDoNothingDecision() {
        setupPriceReferenceRepositoryMockForLitecoinEuro();
        final TradingBearingStrategyDecisionServiceImpl tradingDecisionServiceSpy = Mockito.spy(tradingDecisionService);
        Mockito
        .doReturn   ( Mono.just( new BigDecimal("0.099999" ) ) )
        .when       ( tradingDecisionServiceSpy ).getAmountToBuy( Mockito.any(ApplicationAssetPairTicker.class) );

        StepVerifier
        .create         ( tradingDecisionServiceSpy.getDecision(newApplicationAssetPairTickerForLitecoinEuro( new BigDecimal("44.850000") )))
        .consumeNextWith( applicationAssetPairTickerTradingDecision -> {
            assertEquals( Decision.DO_NOTHING, applicationAssetPairTickerTradingDecision.getOperation().getDecision() );
            assertEquals(
        "the computed amount to buy: <0,099999>, is less than the minimum order: <0,100000>",
                applicationAssetPairTickerTradingDecision.getOperation().getMessage()
            );
            assertNull( applicationAssetPairTickerTradingDecision.getAmount() );
            assertNull( applicationAssetPairTickerTradingDecision.getPrice() );
        })
        .verifyComplete ();
    }

    @Test
    void testGetDecisionWhenLastPriceIsBetweenLowAndHighGap_shouldReturnDoNothingDecision() {
        setupPriceReferenceRepositoryMockForLitecoinEuro();

        StepVerifier
        .create         ( tradingDecisionService.getDecision(newApplicationAssetPairTickerForLitecoinEuro( new BigDecimal("45.62") )))
        .consumeNextWith( applicationAssetPairTickerTradingDecision -> {
            assertEquals( Decision.DO_NOTHING, applicationAssetPairTickerTradingDecision.getOperation().getDecision() );
            assertEquals(
                    "gap: <0,800000> is not reached yet, price reference: <45,650000>, current price: <45,620000>",
                    applicationAssetPairTickerTradingDecision.getOperation().getMessage()
            );
            assertNull( applicationAssetPairTickerTradingDecision.getAmount() );
            assertNull( applicationAssetPairTickerTradingDecision.getPrice() );
        })
        .verifyComplete ();
    }

    @Test
    void testGeAmountToSellWhenAvailableAssetIsGreaterThanConfiguredAmountToSell_shouldReturnConfiguredAmountToSell() {
        Mockito
        .when       ( exchangeService.getAvailableAssetForSellPlacementByApplicationAssetPair("litecoin-euro") )
        .thenReturn ( Mono.just(new BigDecimal("1")) );

        StepVerifier
        .create         ( tradingDecisionService.getAmountToSell("litecoin-euro") )
        .consumeNextWith( amountToSell -> assertEquals( new BigDecimal("0.5"), amountToSell ) )
        .verifyComplete ();
    }

    @Test
    void testGeAmountToSellWhenAvailableAssetIsLessThanConfiguredAmountToSell_shouldReturnAvailableAsset() {
        Mockito
        .when       ( exchangeService.getAvailableAssetForSellPlacementByApplicationAssetPair("litecoin-euro") )
        .thenReturn ( Mono.just(new BigDecimal("0.25")) );

        StepVerifier
        .create         ( tradingDecisionService.getAmountToSell("litecoin-euro") )
        .consumeNextWith( amountToSell -> assertEquals( new BigDecimal("0.25"), amountToSell ) )
        .verifyComplete ();
    }

    @Test
    void testGeAmountToBuyWhenAvailableAssetIsGreaterThanConfiguredAmountToBuy_shouldReturnConfiguredAmountToBuy() {
        Mockito
        .when       ( exchangeService.getAvailableAssetForBuyPlacementByApplicationAssetPair("litecoin-euro") )
        .thenReturn ( Mono.just(new BigDecimal("22.640")) );

        StepVerifier
        .create         ( tradingDecisionService.getAmountToBuy(newApplicationAssetPairTickerForLitecoinEuro(new BigDecimal("45.25"))) )
        .consumeNextWith( amountToBuy -> assertEquals( new BigDecimal("0.5"), amountToBuy ) )
        .verifyComplete ();
    }

    @Test
    void testGeAmountToBuyWhenAvailableAssetIsLessThanConfiguredAmountToBuy_shouldReturnAvailableAsset() {
        Mockito
        .when       ( exchangeService.getAvailableAssetForBuyPlacementByApplicationAssetPair("litecoin-euro") )
        .thenReturn ( Mono.just(new BigDecimal("15.255")) );

        StepVerifier
        .create         ( tradingDecisionService.getAmountToBuy(newApplicationAssetPairTickerForLitecoinEuro(new BigDecimal("45.25"))) )
        .consumeNextWith( amountToBuy -> assertEquals( new BigDecimal("0.33712707"), amountToBuy ) )
        .verifyComplete ();
    }

    private void setupPriceReferenceRepositoryMockForLitecoinEuro() {
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
    }

    private ApplicationAssetPairTicker newApplicationAssetPairTickerForLitecoinEuro(final BigDecimal price) {
        return  ApplicationAssetPairTicker
                .builder()
                .applicationAssetPair ( "litecoin-euro" )
                .price                ( price )
                .build();
    }
}
