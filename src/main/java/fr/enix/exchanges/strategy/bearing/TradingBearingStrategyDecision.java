package fr.enix.exchanges.strategy.bearing;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import reactor.core.publisher.Flux;

public interface TradingBearingStrategyDecision {

    Flux<ApplicationAssetPairTickerTradingDecision> getDecisions(final ApplicationAssetPairTicker applicationAssetPairTicker);
}
