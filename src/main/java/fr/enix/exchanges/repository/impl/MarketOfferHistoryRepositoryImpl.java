package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MarketOfferHistoryRepositoryImpl implements MarketOfferHistoryRepository {

    private final Map<Asset, MarketPriceHistory> records;

    public MarketOfferHistoryRepositoryImpl() {
        records = new HashMap<>();
    }

    @Override
    public Mono<Void> saveNewMarketPrice(final Asset asset, final BigDecimal price) {
        initRecords         (asset);
        updatePreviousPrice (records.get(asset));
        updateCurrentPrice  (records.get(asset), asset, price);
        return Mono.empty();
    }

    @Override
    public Mono<MarketPriceHistory> getMarketPriceHistory(final Asset asset) {
        return Mono.just(records.get(asset));
    }

    private void initRecords(final Asset asset) {
        if(!records.containsKey(asset)) {
            records.put(asset, new MarketPriceHistory());
        }
    }

    private void updatePreviousPrice(final MarketPriceHistory marketPriceHistory) {
        if ( marketPriceHistory.getCurrentMarketPrice() != null) {
            marketPriceHistory.setPreviousMarketPrice(marketPriceHistory.getCurrentMarketPrice()
                                                                        .toBuilder()
                                                                        .build()
            );
        }
    }

    private void updateCurrentPrice(final MarketPriceHistory marketPriceHistory,
                                    final Asset asset,
                                    final BigDecimal price) {

        marketPriceHistory.setCurrentMarketPrice(MarketPriceHistory.MarketPrice.builder     ()
                                                                               .asset       (asset)
                                                                               .price       (price)
                                                                               .date        (LocalDateTime.now())
                                                                               .build       ()
        );
    }
}
