package fr.enix.exchanges.service;

import reactor.core.publisher.Flux;

public interface ApplicationTradingConfigurationService {

    Flux<String> getEnabledAssetPairsRepresentationForWebServiceTrading();
    Flux<String> getEnabledAssetPairsRepresentationForWebSocketTrading();
}
