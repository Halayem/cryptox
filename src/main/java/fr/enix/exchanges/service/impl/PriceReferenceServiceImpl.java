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
    private static final String UPDATED_BY_APPLICATION  = "application";
    private static final String UPDATED_BY_USER         = "user";

    @Override
    public void updatePriceReference(final ApplicationAssetPairTicker applicationAssetPairTicker) {

        priceReferenceRepository.updatePriceReferenceForApplicationAssetPair(
            applicationAssetPairTicker.getApplicationAssetPair(),
            applicationAssetPairTicker.getPrice(),
            UPDATED_BY_APPLICATION
        )
        .subscribe( priceReference -> log.debug("update: {}", priceReference) );
    }

    @Override
    public void updatePriceReference(final fr.enix.exchanges.model.dto.PriceReference priceReference) {
        priceReferenceRepository.updatePriceReferenceForApplicationAssetPair(
            priceReference.getApplicationAssetPair(),
            priceReference.getPrice(),
            UPDATED_BY_USER
        )
        .subscribe( priceReference1 -> log.debug("update: {}", priceReference1) );
    }


    @Override
    public Mono<PriceReference> getPriceReferenceForApplicationAssetPair(String applicationAssetPair) {
        return priceReferenceRepository.getPriceReferenceForApplicationAssetPair(applicationAssetPair);
    }
}
