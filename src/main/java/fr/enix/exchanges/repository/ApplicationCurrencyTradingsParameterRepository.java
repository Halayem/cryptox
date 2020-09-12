package fr.enix.exchanges.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ApplicationCurrencyTradingsParameterRepository {
    Flux<String> getEnabledCurrenciesForTrading();
    Flux<String> getStrategiesByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getPriceGapByApplicationAssetPair(final String applicationAssetPair);
}
