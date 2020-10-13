package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.repository.PriceReference;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PriceReferenceRepositoryImpl implements PriceReferenceRepository  {

    protected Map<String, PriceReference> applicationAssetPairPriceReferences = new ConcurrentHashMap<>();

    @Override
    public Mono<PriceReference> getPriceReferenceForApplicationAssetPair(final String applicationAssetPair) {
        return Mono.justOrEmpty(applicationAssetPairPriceReferences.get(applicationAssetPair));
    }

    @Override
    public Mono<PriceReference> updatePriceReferenceForApplicationAssetPair(final String applicationAssetPair, final BigDecimal newPrice) {
        return
            Mono.just(
                PriceReference
                .builder    ()
                .price      (newPrice)
                .datetime   (LocalDateTime.now())
                .build      ()
            ).flatMap(priceReference -> {
                applicationAssetPairPriceReferences.put( applicationAssetPair, priceReference );
                return Mono.just(priceReference);
            });
    }
}
