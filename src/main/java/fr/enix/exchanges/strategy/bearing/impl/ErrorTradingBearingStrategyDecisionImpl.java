package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ErrorTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final PriceReferenceService priceReferenceService;

    @Override
    public Flux<ApplicationAssetPairTickerTradingDecision> getDecisions(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        priceReferenceService.updatePriceReference(applicationAssetPairTicker);

        return Flux.just(newTradingDecisionError(
                applicationAssetPairTicker,
                String.format( "price reference was not set for this application asset pair: <%s>", applicationAssetPairTicker.getApplicationAssetPair() )
        ));
    }

    private ApplicationAssetPairTickerTradingDecision newTradingDecisionError(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                                    final String message) {
        return
                ApplicationAssetPairTickerTradingDecision
                    .builder    ()
                    .operation  (
                        ApplicationAssetPairTickerTradingDecision
                            .Operation
                            .builder  ()
                            .decision (ApplicationAssetPairTickerTradingDecision.Decision.ERROR)
                            .message  (message)
                            .build    ()
                    )
                    .applicationAssetPairTickerReference(applicationAssetPairTicker.toBuilder().build())
                    .build();
    }
}
