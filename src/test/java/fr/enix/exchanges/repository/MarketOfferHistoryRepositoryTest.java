package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.ws.AssetPair;
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
class MarketOfferHistoryRepositoryTest {

    @Autowired
    private MarketOfferHistoryRepository marketOfferHistoryRepository;

    private final AssetPair     firstMarketOfferAssetPair   = AssetPair.builder().from(Asset.LTC).to(Asset.EUR).build();
    private final BigDecimal    firstMarketOfferPrice       = new BigDecimal("34.12");

    private final AssetPair     secondMarketOfferAssetPair  = AssetPair.builder().from(Asset.LTC).to(Asset.EUR).build();
    private final BigDecimal    secondMarketOfferPrice      = new BigDecimal("32.25");

    @Test
    @Order(0)
    void testGetMarketPriceHistory_shouldNotSendSignalWhenRecordsAreEmptyYet() {
        StepVerifier.create         (marketOfferHistoryRepository.getMarketOfferHistory(firstMarketOfferAssetPair))
                    .verifyComplete ();
    }

    @Test
    @Order(1)
    void testSaveNewMarketPrice_shouldReturnMarketPriceObjectAsNewestMarketOffer() {

        StepVerifier.create(marketOfferHistoryRepository.saveNewMarketOffer(firstMarketOfferAssetPair, firstMarketOfferPrice))
                    .consumeNextWith(marketPriceHistory -> {
                        assertEquals    (firstMarketOfferAssetPair, marketPriceHistory.getCurrentMarketOffer().getAssetPair());
                        assertEquals    (firstMarketOfferPrice,     marketPriceHistory.getCurrentMarketOffer().getPrice());
                    })
                    .verifyComplete ();

    }


    @Test
    @Order(2)
    void testGetMarketOfferHistory_previousShouldBeNullAndNewShouldBeFirstMarketOffer() {
        StepVerifier.create         (marketOfferHistoryRepository.getMarketOfferHistory(firstMarketOfferAssetPair))
                    .consumeNextWith(marketPriceHistory -> {

                        assertNull      (marketPriceHistory.getPreviousMarketOffer());

                        assertEquals    (firstMarketOfferAssetPair, marketPriceHistory.getCurrentMarketOffer().getAssetPair());
                        assertEquals    (firstMarketOfferPrice,     marketPriceHistory.getCurrentMarketOffer().getPrice());
                        assertNotNull   (marketPriceHistory.getCurrentMarketOffer().getDate());
                    })
                    .verifyComplete();
    }

    @Test
    @Order(3)
    void testGetMarketOfferHistory_() {
        StepVerifier.create(marketOfferHistoryRepository
                            .saveNewMarketOffer (secondMarketOfferAssetPair, secondMarketOfferPrice)
                            .flatMap            (marketPrice -> marketOfferHistoryRepository.getMarketOfferHistory(secondMarketOfferAssetPair))
                    )
                    .consumeNextWith(marketPriceHistory -> {
                        assertEquals    (firstMarketOfferAssetPair, marketPriceHistory.getPreviousMarketOffer().getAssetPair());
                        assertEquals    (firstMarketOfferPrice,     marketPriceHistory.getPreviousMarketOffer().getPrice());
                        assertNotNull   (marketPriceHistory.getPreviousMarketOffer().getDate());

                        assertEquals    (secondMarketOfferAssetPair,    marketPriceHistory.getCurrentMarketOffer().getAssetPair());
                        assertEquals    (secondMarketOfferPrice,        marketPriceHistory.getCurrentMarketOffer().getPrice());
                        assertNotNull   (marketPriceHistory.getCurrentMarketOffer().getDate());

                        assertTrue(marketPriceHistory.getCurrentMarketOffer()
                                                     .getDate()
                                                     .isAfter(
                                                         marketPriceHistory.getPreviousMarketOffer()
                                                                           .getDate()
                                                     )
                        );
                    })
                    .verifyComplete();

    }
}
