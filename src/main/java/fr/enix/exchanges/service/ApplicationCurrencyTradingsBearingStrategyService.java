package fr.enix.exchanges.service;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ApplicationCurrencyTradingsBearingStrategyService {

    Mono<BigDecimal> getGapScaleByApplicationAssetPair          (final String applicationAssetPair);
    Mono<BigDecimal> getAmountToBuyByApplicationAssetPair       (final String applicationAssetPair);
    Mono<BigDecimal> getAmountToSellByApplicationAssetPair      (final String applicationAssetPair);
    Mono<BigDecimal> getAmountEnhanceStepByApplicationAssetPair (final String applicationAssetPair);
    Mono<BigDecimal> getBuyStopLossByApplicationAssetPair       (final String applicationAssetPair);

}
