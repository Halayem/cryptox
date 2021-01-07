package fr.enix.exchanges.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static fr.enix.utility.ApplicationDateTimeFormatter.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MarketOfferServiceImplTest {

    @Autowired MarketOfferServiceImpl marketOfferService;

    private final String dateTimeReference = "2021-01-01";

    @Test
    void testNewMarketOfferHistorySearchRequestForRelativeYesterday() {
        StepVerifier
        .create(
            marketOfferService
            .newMarketOfferHistorySearchRequestForRelativeYesterday(
    "litecoin-euro",
                    parseDateFromString(dateTimeReference ))
        ).consumeNextWith(marketOfferHistorySearchRequest -> {
            assertEquals("kraken", marketOfferHistorySearchRequest.getMarketPlace().getValue());
            assertEquals("litecoin-euro", marketOfferHistorySearchRequest.getApplicationAssetPair());
            assertEquals(parseDatetimeFromString("2020-12-31 00:00:00"), marketOfferHistorySearchRequest.getAfter());
            assertEquals(parseDatetimeFromString("2020-12-31 23:59:59"), marketOfferHistorySearchRequest.getBefore());
        }).verifyComplete();
    }
}
