package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
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
    public void checkAndUpdatePriceReference(final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision) {
        switch (applicationAssetPairTickerTradingDecision.getOperation().getDecision()) {
            case BUY    :
            case SELL   :
            case ERROR  : setPriceReferenceAsCurrentPrice(applicationAssetPairTickerTradingDecision); break;
        }
    }

    @Override
    public Mono<PriceReference> getPriceReferenceForApplicationAssetPair(String applicationAssetPair) {
        return priceReferenceRepository.getPriceReferenceForApplicationAssetPair(applicationAssetPair);
    }

    private void setPriceReferenceAsCurrentPrice(final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision) {
        log.debug("updating price reference based on this decision:{}", applicationAssetPairTickerTradingDecision.getFormattedLogMessage());

        priceReferenceRepository.updatePriceReferenceForApplicationAssetPair(
            applicationAssetPairTickerTradingDecision.getApplicationAssetPairTickerReference().getApplicationAssetPair(),
            applicationAssetPairTickerTradingDecision.getApplicationAssetPairTickerReference().getPrice()
        )
        .subscribe( priceReference -> log.debug("price reference updated: {}", priceReference) );
    }
}
