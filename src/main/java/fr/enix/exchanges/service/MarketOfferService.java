package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.model.ws.AssetPair;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferService {

    void resetAllMarketOfferHistory();
    Mono<MarketPriceHistory> saveNewMarketPrice(final AssetPair assetPair, final BigDecimal price);
}
