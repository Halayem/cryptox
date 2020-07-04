package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.model.ws.AssetPair;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferService {

    Mono<MarketPriceHistory.MarketPrice> saveNewMarketPrice(final AssetPair assetPair, final BigDecimal price);
    Mono<MarketPriceHistory> getMarketPriceHistory(final AssetPair assetPair);
}
