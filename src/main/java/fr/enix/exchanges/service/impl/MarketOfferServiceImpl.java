package fr.enix.exchanges.service.impl;

import fr.enix.common.MarketPlace;
import fr.enix.exchanges.model.business.MarketExtremumPrice;
import fr.enix.exchanges.model.business.MarketOfferHistorySearchRequest;
import fr.enix.exchanges.model.business.MarketOfferHistorySearchResponse;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.MarketPlaceService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class MarketOfferServiceImpl implements MarketOfferService {

    private final MarketOfferHistoryRepository marketOfferHistoryRepository;
    private final MarketPlaceService marketPlaceService;
    private final MarketOfferHelper marketOfferHelper;

    public MarketOfferServiceImpl(final MarketOfferHistoryRepository marketOfferHistoryRepository,
                                  final MarketPlaceService marketPlaceService) {

        this.marketOfferHistoryRepository   = marketOfferHistoryRepository;
        this.marketPlaceService             = marketPlaceService;
        this.marketOfferHelper              = new MarketOfferHelper(this.marketPlaceService.getMarketPlace());
    }

    @Override
    public Mono<Void> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price) {
        return marketOfferHistoryRepository.saveApplicationAssetPairTicker(
            marketPlaceService.getMarketPlace(),
            applicationAssetPair,
            price
        );
    }

    @Override
    public Mono<MarketExtremumPrice> getTickerExtremumPriceOfYesterday(final String applicationAssetPair) {
        return  marketOfferHelper
                .newMarketOfferHistorySearchRequestForYesterday(applicationAssetPair)
                .flatMap(this::getTickerExtremumPriceOfYesterday);

    }

    private Mono<MarketExtremumPrice> getTickerExtremumPriceOfYesterday(final MarketOfferHistorySearchRequest marketOfferHistorySearchRequest) {

        return Mono.zip(
            marketOfferHistoryRepository.getLowestOffer     (marketOfferHistorySearchRequest),
            marketOfferHistoryRepository.getHighestOffer    (marketOfferHistorySearchRequest)
        ).map(objects -> {
            final MarketOfferHistorySearchResponse lowestOfferSearchResponse    = objects.getT1();
            final MarketOfferHistorySearchResponse highestOfferSearchResponse   = objects.getT2();
            return MarketExtremumPrice
                    .builder()
                    .highest(highestOfferSearchResponse.getPrice())
                    .lowest (lowestOfferSearchResponse.getPrice())
                    .build  ();
        });
    }

    @AllArgsConstructor
    protected static class MarketOfferHelper {

        private final MarketPlace marketPlace;

        protected Mono<MarketOfferHistorySearchRequest> newMarketOfferHistorySearchRequestForYesterday(final String applicationAssetPair) {
            final LocalDate yesterday = LocalDate.now().minusDays(1L);
            return Mono.just(
                    MarketOfferHistorySearchRequest
                    .builder                ()
                    .marketPlace            ( marketPlace                    )
                    .applicationAssetPair   ( applicationAssetPair           )
                    .after                  ( yesterday.atStartOfDay()       )
                    .before                 ( yesterday.atTime(LocalTime.MAX))
                    .build()
            );
        }
    }
}
