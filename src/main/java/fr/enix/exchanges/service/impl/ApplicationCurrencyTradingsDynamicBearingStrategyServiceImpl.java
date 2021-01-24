package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.service.ApplicationCurrencyTradingsBearingStrategyService;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class ApplicationCurrencyTradingsDynamicBearingStrategyServiceImpl implements ApplicationCurrencyTradingsBearingStrategyService {

    @Override
    public Mono<BigDecimal> getGapScaleByApplicationAssetPair(String applicationAssetPair) {
        throw new RuntimeException("method not yet implemented");
    }

    @Override
    public Mono<BigDecimal> getAmountToBuyByApplicationAssetPair(String applicationAssetPair) {
        throw new RuntimeException("method not yet implemented");
    }

    @Override
    public Mono<BigDecimal> getAmountToSellByApplicationAssetPair(String applicationAssetPair) {
        throw new RuntimeException("method not yet implemented");
    }

    @Override
    public Mono<BigDecimal> getAmountEnhanceStepByApplicationAssetPair(String applicationAssetPair) {
        throw new RuntimeException("method not yet implemented");
    }
}
