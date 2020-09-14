package fr.enix.exchanges.repository;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ApplicationThresholdStrategyParameterRepository {

    Mono<BigDecimal> getTriggerPriceToBuyByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getAmountToBuyByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getTriggerPriceToSellByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getAmountToSellByApplicationAssetPair(final String applicationAssetPair);
}
