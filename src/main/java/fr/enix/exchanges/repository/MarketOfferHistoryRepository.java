package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferHistoryRepository {

    Mono<ApplicationAssetPairTicker> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price);
    Mono<BigDecimal> getLastPriceByApplicationAssetPair(final String applicationAssetPair);
}
