package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import fr.enix.exchanges.service.MarketOfferService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class MarketOfferServiceImpl implements MarketOfferService {

    private final MarketOfferHistoryRepository marketOfferHistoryRepository;

    @Override
    public Mono<String> saveApplicationAssetPairTicker(final String applicationAssetPair, final BigDecimal price) {
        return marketOfferHistoryRepository.saveApplicationAssetPairTicker(applicationAssetPair, price)
               .map(applicationAssetPairTicker -> applicationAssetPairTicker.getApplicationAssetPair());
    }

    @Override
    public Mono<BigDecimal> getLastPriceByApplicationAssetPair(final String applicationAssetPair) {
        return marketOfferHistoryRepository.getLastPriceByApplicationAssetPair(applicationAssetPair);
    }
}
