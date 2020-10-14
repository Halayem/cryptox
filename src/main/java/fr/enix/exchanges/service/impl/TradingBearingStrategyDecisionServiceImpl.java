package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.service.TradingDecisionService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecisionFactory;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TradingBearingStrategyDecisionServiceImpl implements TradingDecisionService {

    private final TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactory;

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(final ApplicationAssetPairTicker applicationAssetPairTicker) {

        return  tradingBearingStrategyDecisionFactory
                .getTradingBearingStrategy  (applicationAssetPairTicker)
                .flatMap                    (tradingBearingStrategyDecision -> tradingBearingStrategyDecision.getDecision(applicationAssetPairTicker));

    }

}
