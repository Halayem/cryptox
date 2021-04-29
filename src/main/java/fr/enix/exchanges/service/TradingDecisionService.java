package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import reactor.core.publisher.Flux;

public interface TradingDecisionService {
    Flux<ApplicationAssetPairTickerTradingDecision> getDecisions(final ApplicationAssetPairTicker applicationAssetPairTicker);
}
