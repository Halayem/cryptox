package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MarketOfferHistoryRepositoryImplTest {

    @Autowired
    private MarketOfferHistoryRepositoryImpl marketOfferHistoryRepository;

    @Test
    @Order(0)
    void testGetLastPriceByApplicationAssetPair_shouldReturnMonoEmptyWhenNoPriceWasStoredYet() {
        StepVerifier
        .create(marketOfferHistoryRepository.getLastPriceByApplicationAssetPair("litecoin-euro"))
        .verifyComplete();
    }

    private Flux<ApplicationAssetPairTicker> initRecordsForLitecoinEuroAssetPair() {
        return Flux.concat(
            marketOfferHistoryRepository.saveApplicationAssetPairTicker("litecoin-euro", new BigDecimal("45.5"  )),
            marketOfferHistoryRepository.saveApplicationAssetPairTicker("litecoin-euro", new BigDecimal("41.245")),
            marketOfferHistoryRepository.saveApplicationAssetPairTicker("litecoin-euro", new BigDecimal("41.58" )),
            marketOfferHistoryRepository.saveApplicationAssetPairTicker("litecoin-euro", new BigDecimal("40.12" ))
        ).delayElements(Duration.ofMillis(10l));
    }

    private Flux<ApplicationAssetPairTicker> initRecordsForRippleEuroAssetPair() {
        return Flux.concat(
                marketOfferHistoryRepository.saveApplicationAssetPairTicker("ripple-euro", new BigDecimal("0.215"   )),
                marketOfferHistoryRepository.saveApplicationAssetPairTicker("ripple-euro", new BigDecimal("0.224"   )),
                marketOfferHistoryRepository.saveApplicationAssetPairTicker("ripple-euro", new BigDecimal("0.54"    )),
                marketOfferHistoryRepository.saveApplicationAssetPairTicker("ripple-euro", new BigDecimal("0.185"   ))
        ).delayElements(Duration.ofMillis(10l));
    }

    @Test
    @Order(1)
    void testSaveNewMarketOffersForLitecoinEuroApplicationAssetPair() {
        StepVerifier
        .create(initRecordsForLitecoinEuroAssetPair())
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("45.5"     )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("41.245"   )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("41.58"    )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("40.12"    )))
        .expectComplete()
        .verify();
    }

    @Test
    @Order(2)
    void testSavedMarketOffersForLitecoinEuroApplicationAssetPair() {
        StepVerifier
        .create(marketOfferHistoryRepository.records.get("litecoin-euro"))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("45.5"     )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("41.245"   )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("41.58"    )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("40.12"    )))
        .expectComplete()
        .verify();
    }

    @Test
    @Order(3)
    void testGetLastPriceByApplicationAssetPairForLitecoinEuroApplicationAssetPair() {
        StepVerifier
        .create(marketOfferHistoryRepository.getLastPriceByApplicationAssetPair("litecoin-euro"))
        .consumeNextWith(price -> {
            assertEquals(new BigDecimal("40.12" ), price);
        })
        .verifyComplete();
    }

    @Test
    @Order(4)
    void testSaveNewMarketOffersForRippleEuroApplicationAssetPair() {
        StepVerifier
        .create(initRecordsForRippleEuroAssetPair())
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("0.215"    )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("0.224"    )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("0.54"     )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("0.185"    )))
        .expectComplete()
        .verify();
    }

    @Test
    @Order(5)
    void testSavedMarketOffersForRippleEuroApplicationAssetPair() {
        StepVerifier
        .create(marketOfferHistoryRepository.records.get("ripple-euro"))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("0.215"    )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("0.224"    )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("0.54"     )))
        .expectNextMatches(applicationAssetPairMarketTicker -> applicationAssetPairMarketTicker.getPrice().equals(new BigDecimal("0.185"    )))
        .expectComplete()
        .verify();
    }

    @Test
    @Order(6)
    void testGetLastPriceByApplicationAssetPairForRippleEuroApplicationAssetPair() {
        StepVerifier
        .create(marketOfferHistoryRepository.getLastPriceByApplicationAssetPair("ripple-euro"))
        .consumeNextWith(price -> {
            assertEquals(new BigDecimal("0.185" ), price);
        })
        .verifyComplete();
    }

    @Test
    @Order(7)
    void testMarketOfferHistoryRepositoryImplRecordsHasOnly2Entries() {
        assertEquals(2, marketOfferHistoryRepository.records.size());
    }
}
