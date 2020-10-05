package fr.enix.exchanges.repository;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ApplicationBearingStrategyParameterRepository {

    Mono<BigDecimal> getGapByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getAmountToSellByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getAmountToBuyByApplicationAssetPair(final String applicationAssetPair);
}
