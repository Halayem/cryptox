package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.PriceReference;
import reactor.core.publisher.Mono;

public interface PriceReferenceService {

    void checkAndUpdatePriceReference(final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision);
    Mono<PriceReference> getPriceReferenceForApplicationAssetPair(final String applicationAssetPair);
}
