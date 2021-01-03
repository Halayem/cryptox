package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.MarketExtremumPrice;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferService {

    Mono<Void> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price);
    Mono<MarketExtremumPrice> getTickerExtremumPriceOfYesterday(final String applicationAssetPair);
}
