package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface TradingDecisionService {

    Mono<ApplicationAssetPairTickerTradingDecision> getDecision(final ApplicationAssetPairTicker applicationAssetPairTicker);
    Mono<BigDecimal> getAmountToSell(final String applicationAssetPair);
    Mono<BigDecimal> getAmountToBuy(final ApplicationAssetPairTicker applicationAssetPairTicker);
}
