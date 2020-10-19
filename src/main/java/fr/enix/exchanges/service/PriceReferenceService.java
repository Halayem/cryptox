package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.model.repository.PriceReference;
import reactor.core.publisher.Mono;

public interface PriceReferenceService {

    /**
     * Updated by system when price hit the gap: low or down
     */
    void updatePriceReference(final ApplicationAssetPairTicker applicationAssetPairTicker);

    /**
     * Updated by user
     */
    void updatePriceReference(final fr.enix.exchanges.model.dto.PriceReference priceReference);

    Mono<PriceReference> getPriceReferenceForApplicationAssetPair(final String applicationAssetPair);
}
