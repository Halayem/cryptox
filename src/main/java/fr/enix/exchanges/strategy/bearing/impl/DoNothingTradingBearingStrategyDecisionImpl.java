package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import reactor.core.publisher.Mono;

public class DoNothingTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(ApplicationAssetPairTicker applicationAssetPairTicker) {
        return newTradingDecisionWhenDoNothing(
                applicationAssetPairTicker,
                String.format( "price: <%f> (%s) did not reach the gap", applicationAssetPairTicker.getPrice(), applicationAssetPairTicker.getApplicationAssetPair())
        );
    }

    private Mono<ApplicationAssetPairTickerTradingDecision> newTradingDecisionWhenDoNothing(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                                            final String message) {
        return Mono.just(
                ApplicationAssetPairTickerTradingDecision
                    .builder    ()
                    .operation  (
                        ApplicationAssetPairTickerTradingDecision
                            .Operation
                            .builder  ()
                            .decision (ApplicationAssetPairTickerTradingDecision.Decision.DO_NOTHING)
                            .message  (message)
                            .build    ()
                    )
                    .applicationAssetPairTickerReference( applicationAssetPairTicker.toBuilder().build() )
                    .build()
        );
    }
}
