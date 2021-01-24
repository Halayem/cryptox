package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.TickerHistory;
import fr.enix.exchanges.repository.TickerHistoryRepository;
import fr.enix.exchanges.service.MarketPlaceService;
import fr.enix.exchanges.service.PriceVariationService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MarketOfferServiceDBAccessOptimizerImpl extends MarketOfferServiceImpl {

    private final PriceVariationService     priceVariationService;
    private final PriceInMemoryRepository   priceInMemoryRepository;


    public MarketOfferServiceDBAccessOptimizerImpl(final MarketPlaceService         marketPlaceService,
                                                   final PriceVariationService      priceVariationService,
                                                   final TickerHistoryRepository    tickerHistoryRepository) {
        super(marketPlaceService, tickerHistoryRepository);
        this.priceVariationService      = priceVariationService;
        this.priceInMemoryRepository    = new PriceInMemoryRepository(marketPlaceService.getMarketPlace().getValue());
    }

    @Override
    public Mono<TickerHistory> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price) {
        return
            Mono
            .just   ( priceVariationService.arePricesAboutEquals( priceInMemoryRepository.getPrice( applicationAssetPair ), price ))
            .flatMap( arePricesAboutEquals -> {
                final TickerHistory tickerHistory = TickerHistory.builder    ()
                                                                 .market     ( marketPlaceService.getMarketPlace().getValue() )
                                                                 .assetPair  ( applicationAssetPair  )
                                                                 .price      ( price                 )
                                                                 .at         ( LocalDateTime.now()   )
                                                                 .build      ();

                if ( arePricesAboutEquals ) {
                    log.debug(  "prices are about equals for market: <{}>, application asset pair: <{}>, stored price: <{}>, current price: <{}>, storing in database will be escaped",
                                marketPlaceService.getMarketPlace().getValue(), applicationAssetPair, priceInMemoryRepository.getPrice( applicationAssetPair ), price );

                    return Mono.just(tickerHistory);
                } else {
                    log.info(   "prices are not about equals for market: <{}>, application asset pair: <{}>, stored price: <{}>, current price: <{}>, storing in database will be processed",
                                marketPlaceService.getMarketPlace().getValue(), applicationAssetPair, priceInMemoryRepository.getPrice( applicationAssetPair ), price );

                    priceInMemoryRepository.savePrice( applicationAssetPair, price );
                    return tickerHistoryRepository.save( tickerHistory );
                }
            });
    }

    private static class PriceInMemoryRepository {

        private final String marketPlace;
        private Map<String, BigDecimal> prices;

        PriceInMemoryRepository(final String marketPlace) {
            this.marketPlace    = marketPlace;
            this.prices         = new HashMap<>();
        }

        public BigDecimal getPrice( final String applicationAssetPair ) {
            return prices.get( buildRepositoryKey( applicationAssetPair ) );
        }

        public void savePrice(final String applicationAssetPair, final BigDecimal price ) {
            prices.put( buildRepositoryKey( applicationAssetPair ), price );
        }

        private String buildRepositoryKey(final String applicationAssetPair) {
            return String.format( "%s:%s", marketPlace, applicationAssetPair );
        }
    }

}
