package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.Locale;

@AllArgsConstructor
public class DoNothingTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper;

    @Override
    public Flux<ApplicationAssetPairTickerTradingDecision> getDecisions(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        return Flux.just(applicationAssetPairTickerMapper.mapDoNothingDecision(
                applicationAssetPairTicker,
                String.format( Locale.FRANCE, "price: <%.6f> (%s) did not reach the gap", applicationAssetPairTicker.getPrice(), applicationAssetPairTicker.getApplicationAssetPair())
        ));
    }
}
