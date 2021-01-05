package fr.enix.exchanges.service.impl;

import fr.enix.common.MarketPlace;
import fr.enix.exchanges.model.business.MarketExtremumPrice;
import fr.enix.exchanges.model.business.MarketOfferHistorySearchRequest;
import fr.enix.exchanges.model.repository.Ticker;
import fr.enix.exchanges.repository.TickerHistoryRepository;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.MarketPlaceService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MarketOfferServiceImpl implements MarketOfferService {

    private final TickerHistoryRepository   tickerHistoryRepository;
    private final MarketPlaceService        marketPlaceService;
    private final MarketOfferHelper         marketOfferHelper;

    public MarketOfferServiceImpl(final MarketPlaceService marketPlaceService,
                                  final TickerHistoryRepository tickerHistoryRepository) {

        this.tickerHistoryRepository    = tickerHistoryRepository;
        this.marketPlaceService         = marketPlaceService;
        this.marketOfferHelper          = new MarketOfferHelper(this.marketPlaceService.getMarketPlace());
    }

    @Override
    public Mono<Ticker> saveApplicationAssetPairTicker( final String applicationAssetPair, final BigDecimal price ) {
        return tickerHistoryRepository.save(
                    Ticker
                    .builder    ()
                    .market     ( marketPlaceService.getMarketPlace().getValue() )
                    .assetPair  ( applicationAssetPair  )
                    .price      ( price                 )
                    .at         ( LocalDateTime.now()   )
                    .build      ()
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
            tickerHistoryRepository.getLowestPrice( marketOfferHistorySearchRequest ),
            tickerHistoryRepository.getHighestPrice( marketOfferHistorySearchRequest )
        ).map(objects -> MarketExtremumPrice.builder().lowest( objects.getT1() ).highest( objects.getT2() ).build());
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
