package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.model.ws.AssetPair;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MarketOfferHistoryRepositoryImpl implements MarketOfferHistoryRepository {

    private Map<AssetPair, MarketPriceHistory> records;

    public MarketOfferHistoryRepositoryImpl() {
        records = new HashMap<>();
    }

    @Override
    public void resetAllMarketOfferHistory() {
        records = new HashMap<>();
    }

    @Override
    public Mono<MarketPriceHistory> saveNewMarketOffer(final AssetPair assetPair, final BigDecimal price) {
        initRecords         (assetPair);
        updatePreviousPrice (records.get(assetPair));
        updateCurrentPrice  (records.get(assetPair), assetPair, price);

        return Mono.just(records.get(assetPair));
    }

    @Override
    public Mono<MarketPriceHistory> getMarketOfferHistory(final AssetPair assetPair) {
        return Mono.justOrEmpty(records.get(assetPair));
    }

    private void initRecords(final AssetPair assetPair) {
        if(!records.containsKey(assetPair)) {
            records.put(assetPair, new MarketPriceHistory());
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
                                    final AssetPair assetPair,
                                    final BigDecimal price) {

        marketPriceHistory.setCurrentMarketOffer(MarketPriceHistory.MarketPrice.builder     ()
                                                                               .assetPair   (assetPair)
                                                                               .price       (price)
                                                                               .date        (LocalDateTime.now())
                                                                               .build       ()
        );
    }
}
