package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.model.repository.PriceReference;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.service.PriceReferenceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
public class PriceReferenceServiceImpl implements PriceReferenceService {

    private final PriceReferenceRepository priceReferenceRepository;

    @Override
    public void updatePriceReference(final ApplicationAssetPairTicker applicationAssetPairTicker, final String updatedBy) {

        priceReferenceRepository.updatePriceReferenceForApplicationAssetPair(
                applicationAssetPairTicker.getApplicationAssetPair(),
                applicationAssetPairTicker.getPrice(),
                updatedBy
        )
        .subscribe( priceReference -> log.debug("price reference updated, price: {}, updated by: {}", priceReference, updatedBy) );
    }

    @Override
    public Mono<PriceReference> getPriceReferenceForApplicationAssetPair(String applicationAssetPair) {
        return priceReferenceRepository.getPriceReferenceForApplicationAssetPair(applicationAssetPair);
    }
}
