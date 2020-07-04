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
    public Mono<MarketPriceHistory.MarketPrice> saveNewMarketOffer(final Asset asset, final BigDecimal price) {
        initRecords         (asset);
        updatePreviousPrice (records.get(asset));
        updateCurrentPrice  (records.get(asset), asset, price);

        return Mono.just(
                records.get(asset)
                       .getCurrentMarketOffer()
        );
    }

    @Override
    public Mono<MarketPriceHistory> getMarketOfferHistory(final Asset asset) {
        return Mono.justOrEmpty(records.get(asset));
    }

    private void initRecords(final Asset asset) {
        if(!records.containsKey(asset)) {
            records.put(asset, new MarketPriceHistory());
        }
    }

    private void updatePreviousPrice(final MarketPriceHistory marketPriceHistory) {
        if ( marketPriceHistory.getCurrentMarketOffer() != null) {
            marketPriceHistory.setPreviousMarketOffer(marketPriceHistory.getCurrentMarketOffer()
                                                                        .toBuilder()
                                                                        .build()
            );
        }
    }

    private void updateCurrentPrice(final MarketPriceHistory marketPriceHistory,
                                    final Asset asset,
                                    final BigDecimal price) {

        marketPriceHistory.setCurrentMarketOffer(MarketPriceHistory.MarketPrice.builder     ()
                                                                               .asset       (asset)
                                                                               .price       (price)
                                                                               .date        (LocalDateTime.now())
                                                                               .build       ()
        );
    }
}
