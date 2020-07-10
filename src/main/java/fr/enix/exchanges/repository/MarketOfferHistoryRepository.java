package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.model.ws.AssetPair;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferHistoryRepository {

    void resetAllMarketOfferHistory();
    Mono<MarketPriceHistory> saveNewMarketOffer(final AssetPair assetPair, final BigDecimal price);
    Mono<MarketPriceHistory> getMarketOfferHistory(final AssetPair assetPair);

}
