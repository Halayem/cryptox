package fr.enix.exchanges.repository;

import reactor.core.publisher.Flux;

public interface ApplicationCurrencyTradingsParameterRepository {
    Flux<String> getEnabledApplicationAssetPairForTrading();
    Flux<String> getStrategiesByApplicationAssetPair(final String applicationAssetPair);
}
