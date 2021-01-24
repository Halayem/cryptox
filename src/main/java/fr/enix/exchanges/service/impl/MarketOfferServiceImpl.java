package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.MarketExtremumPrice;
import fr.enix.exchanges.model.business.MarketOfferHistorySearchRequest;
import fr.enix.exchanges.model.repository.TickerHistory;
import fr.enix.exchanges.repository.TickerHistoryRepository;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.MarketPlaceService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
public class MarketOfferServiceImpl implements MarketOfferService {

    protected final MarketPlaceService        marketPlaceService;
    protected final TickerHistoryRepository   tickerHistoryRepository;

    @Override
    public Mono<TickerHistory> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price) {
        return tickerHistoryRepository.save(
                    TickerHistory
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
        return  newMarketOfferHistorySearchRequestForRelativeYesterday(applicationAssetPair, LocalDate.now())
                .flatMap(this::getTickerExtremumPriceOfYesterday);
    }

    protected Mono<MarketOfferHistorySearchRequest> newMarketOfferHistorySearchRequestForRelativeYesterday(final String applicationAssetPair,
                                                                                                           final LocalDate dateReference) {
        final LocalDate yesterday = dateReference.minusDays(1L);
        return Mono.just(
                MarketOfferHistorySearchRequest
                        .builder                ()
                        .marketPlace            ( marketPlaceService.getMarketPlace()   )
                        .applicationAssetPair   ( applicationAssetPair                  )
                        .after                  ( yesterday.atStartOfDay()              )
                        .before                 ( yesterday.atTime(23, 59, 59)       )
                        .build()
        );
    }

    private Mono<MarketExtremumPrice> getTickerExtremumPriceOfYesterday(final MarketOfferHistorySearchRequest marketOfferHistorySearchRequest) {
        return Mono.zip(
            tickerHistoryRepository.getLowestPrice( marketOfferHistorySearchRequest ),
            tickerHistoryRepository.getHighestPrice( marketOfferHistorySearchRequest )
        ).map(objects -> MarketExtremumPrice.builder().lowest( objects.getT1() ).highest( objects.getT2() ).build());
    }
}
