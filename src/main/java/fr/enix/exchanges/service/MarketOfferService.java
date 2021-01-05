package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.MarketExtremumPrice;
import fr.enix.exchanges.model.repository.Ticker;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferService {

    Mono<Ticker> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price);
    Mono<MarketExtremumPrice> getTickerExtremumPriceOfYesterday(final String applicationAssetPair);
}
