package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.parameters.Asset;
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
public class MarketOfferHistoryRepositoryTest {

    @Autowired
    private MarketOfferHistoryRepository marketOfferHistoryRepository;

    private final Asset         firstMarketOfferAsset = Asset.EUR;
    private final BigDecimal    firstMarketOfferPrice = new BigDecimal("34.12");

    private final Asset         secondMarketOfferAsset = Asset.EUR;
    private final BigDecimal    secondMarketOfferPrice = new BigDecimal("32.25");

    @Test
    @Order(0)
    public void testGetMarketPriceHistory_shouldNotSendSignalForUnknownAsset() {
        StepVerifier.create         (marketOfferHistoryRepository.getMarketOfferHistory(Asset.EUR))
                    .verifyComplete ();
    }

    @Test
    @Order(1)
    public void testSaveNewMarketPrice_shouldReturnMarketPriceObjectAsNewestMarketOffer() {

        StepVerifier.create (marketOfferHistoryRepository.saveNewMarketOffer(firstMarketOfferAsset, firstMarketOfferPrice))
                    .consumeNextWith(marketPrice -> {
                        assertEquals    (firstMarketOfferAsset, marketPrice.getAsset());
                        assertEquals    (firstMarketOfferPrice, marketPrice.getPrice());
                    })
                    .verifyComplete ();

    }


    @Test
    @Order(2)
    public void testGetMarketOfferHistory_previousShouldBeNullAndNewShouldBeFirstMarketOffer() {
        StepVerifier.create         (marketOfferHistoryRepository.getMarketOfferHistory(Asset.EUR))
                    .consumeNextWith(marketPriceHistory -> {

                        assertNull      (marketPriceHistory.getPreviousMarketOffer());

                        assertEquals    (firstMarketOfferAsset, marketPriceHistory.getCurrentMarketOffer().getAsset());
                        assertEquals    (firstMarketOfferPrice, marketPriceHistory.getCurrentMarketOffer().getPrice());
                        assertNotNull   (marketPriceHistory.getCurrentMarketOffer().getDate());
                    })
                    .verifyComplete();
    }

    @Test
    @Order(3)
    public void testGetMarketOfferHistory_() {
        StepVerifier.create(marketOfferHistoryRepository
                            .saveNewMarketOffer (secondMarketOfferAsset, secondMarketOfferPrice)
                            .flatMap            (marketPrice -> marketOfferHistoryRepository.getMarketOfferHistory(secondMarketOfferAsset))
                    )
                    .consumeNextWith(marketPriceHistory -> {
                        assertEquals    (firstMarketOfferAsset, marketPriceHistory.getPreviousMarketOffer().getAsset());
                        assertEquals    (firstMarketOfferPrice, marketPriceHistory.getPreviousMarketOffer().getPrice());
                        assertNotNull   (marketPriceHistory.getPreviousMarketOffer().getDate());

                        assertEquals    (secondMarketOfferAsset, marketPriceHistory.getCurrentMarketOffer().getAsset());
                        assertEquals    (secondMarketOfferPrice, marketPriceHistory.getCurrentMarketOffer().getPrice());
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
