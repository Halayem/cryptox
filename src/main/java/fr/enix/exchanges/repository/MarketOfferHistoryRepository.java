package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.repository.MarketPriceHistory;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferHistoryRepository {

    Mono<Void> saveNewMarketPrice(final Asset asset, final BigDecimal price);
    Mono<MarketPriceHistory> getMarketPriceHistory(final Asset asset);

}
