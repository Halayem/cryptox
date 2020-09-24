package fr.enix.exchanges.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ApplicationCurrencyTradingsParameterRepository {
    Flux<String> getEnabledApplicationAssetPairForTrading();
    Flux<String> getStrategiesByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getGapScaleByApplicationAssetPair(final String applicationAssetPair);
}
