package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class MarketOfferHistoryRepositoryImpl implements MarketOfferHistoryRepository {

    /**
     * Key is the application asset pair
     */
    protected Map<String, Flux<ApplicationAssetPairTicker>> records = new ConcurrentHashMap();

    @Override
    public Mono<ApplicationAssetPairTicker> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price) {
        return  buildApplicationAssetPairTicker( applicationAssetPair, price )
                .map( applicationAssetPairTicker -> {
                    saveApplicationAssetPairTickerInRecords(applicationAssetPairTicker);
                    return applicationAssetPairTicker;
                });
    }

    @Override
    public Mono<BigDecimal> getLastPriceByApplicationAssetPair(final String applicationAssetPair) {
        return records
                .get    (applicationAssetPair)
                .reduce ((applicationAssetPairTicker1, applicationAssetPairTicker2) ->
                        (applicationAssetPairTicker1.compareTo(applicationAssetPairTicker2) > 0 ) ? applicationAssetPairTicker1 : applicationAssetPairTicker2
                )
                .map(applicationAssetPairTicker -> applicationAssetPairTicker.getPrice());
    }

    private void saveApplicationAssetPairTickerInRecords(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        records.put(
            applicationAssetPairTicker.getApplicationAssetPair(),
            records.get(applicationAssetPairTicker.getApplicationAssetPair()) == null
                ? Flux.just(applicationAssetPairTicker)
                : Flux.concat(records.get(applicationAssetPairTicker.getApplicationAssetPair()), Flux.just(applicationAssetPairTicker))
        );
    }

    private Mono<ApplicationAssetPairTicker> buildApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price) {
        return
            Mono.just(
                ApplicationAssetPairTicker
                .builder                ()
                .price                  (price                  )
                .applicationAssetPair   (applicationAssetPair   )
                .dateTime               (LocalDateTime.now()    )
                .build                  ()
            );
    }

}
