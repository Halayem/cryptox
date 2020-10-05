package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.PriceReference;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface PriceReferenceRepository {

    Mono<PriceReference> getPriceReferenceForApplicationAssetPair(final String applicationAssetPair);
    Mono<PriceReference> updatePriceReferenceForApplicationAssetPair(final String applicationAssetPair, final BigDecimal priceReference);
}
