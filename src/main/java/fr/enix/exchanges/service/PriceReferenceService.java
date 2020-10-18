package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.model.repository.PriceReference;
import reactor.core.publisher.Mono;

public interface PriceReferenceService {

    void updatePriceReference(final ApplicationAssetPairTicker applicationAssetPairTicker, final String updatedBy);
    Mono<PriceReference> getPriceReferenceForApplicationAssetPair(final String applicationAssetPair);
}
