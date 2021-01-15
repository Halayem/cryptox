package fr.enix.exchanges.strategy.bearing;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface AmountEnhancerService {

    Mono<BigDecimal> getNewAmountEnhanceForSell(final String applicationAssetPair);
    Mono<BigDecimal> getNewAmountEnhanceForBuy(final String applicationAssetPair);
}
