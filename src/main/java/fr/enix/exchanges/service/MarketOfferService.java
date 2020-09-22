package fr.enix.exchanges.service;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferService {

    Mono<Void> saveNewMarketOffer(final String applicationAssetPair, final BigDecimal price);
    Mono<BigDecimal> getLastPriceByApplicationAssetPair(final String applicationAssetPair);
}
