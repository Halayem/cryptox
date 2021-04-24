package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.service.TradingDecisionService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecisionFactory;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class TradingBearingStrategyDecisionServiceImpl implements TradingDecisionService {

    private final TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactory;

    @Override
    public Flux<ApplicationAssetPairTickerTradingDecision> getDecisions(final ApplicationAssetPairTicker applicationAssetPairTicker) {

        return  tradingBearingStrategyDecisionFactory
                .getTradingBearingStrategy  (applicationAssetPairTicker)
                .flatMapMany                (tradingBearingStrategyDecision -> tradingBearingStrategyDecision.getDecisions(applicationAssetPairTicker));

    }

}
