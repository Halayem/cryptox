package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class DoNothingTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper;

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        return applicationAssetPairTickerMapper.mapDoNothingDecision(
                applicationAssetPairTicker,
                String.format( "price: <%f> (%s) did not reach the gap", applicationAssetPairTicker.getPrice(), applicationAssetPairTicker.getApplicationAssetPair())
        );
    }
}
