package fr.enix.exchanges.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ApplicationCurrencyTradingsParameterRepository {
    Flux<String> getEnabledApplicationAssetPairForTrading();
    Mono<ApplicationCurrencyTradingsStrategy> getStrategyForApplicationAssetPair(final String applicationAssetPair);

    // -------------------------------------------------------------------------------------------------------
    // --------------------------- B E A R I N G   S T R A T E G Y -------------------------------------------
    // -------------------------------------------------------------------------------------------------------
    Mono<BigDecimal> getGapScaleByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getAmountToBuyForBearingStrategyByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getAmountToSellForBearingStrategyByApplicationAssetPair(final String applicationAssetPair);
    BigDecimal getAmountEnhanceStepByApplicationAssetPair(final String applicationAssetPair);
}
