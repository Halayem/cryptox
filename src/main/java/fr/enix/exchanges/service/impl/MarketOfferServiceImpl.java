package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.model.ws.AssetPair;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import fr.enix.exchanges.service.MarketOfferService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class MarketOfferServiceImpl implements MarketOfferService {

    private final MarketOfferHistoryRepository marketOfferHistoryRepository;

    @Override
    public Mono<MarketPriceHistory.MarketPrice> saveNewMarketPrice(final AssetPair assetPair, final BigDecimal price) {
        return marketOfferHistoryRepository.saveNewMarketOffer(assetPair, price);
    }

    @Override
    public Mono<MarketPriceHistory> getMarketPriceHistory(final AssetPair assetPair) {
        return marketOfferHistoryRepository.getMarketOfferHistory(assetPair);
    }


}
