package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import fr.enix.exchanges.service.MarketOfferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
@Slf4j
public class MarketOfferServiceImpl implements MarketOfferService {

    private final MarketOfferHistoryRepository marketOfferHistoryRepository;

    @Override
    public Mono<Void> saveNewMarketOffer(final String applicationAssetPair, final BigDecimal price) {
        //return marketOfferHistoryRepository.saveNewMarketOffer(applicationAssetPair, price );
        return Mono.empty();
    }

    @Override
    public Mono<BigDecimal> getLastPriceByApplicationAssetPair(final String applicationAssetPair) {
        return marketOfferHistoryRepository.getLastPriceByApplicationAssetPair(applicationAssetPair);
    }
}
