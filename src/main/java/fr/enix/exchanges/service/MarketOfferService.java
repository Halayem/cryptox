package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferService {

    Mono<ApplicationAssetPairTicker> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price);
    Mono<BigDecimal> getLastPriceByApplicationAssetPair(final String applicationAssetPair);
}
