package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.Decision;
import reactor.core.publisher.Mono;

public interface TradingDecisionService {

    Mono<Decision> getDecision(final String ApplicationAssetPair);
}
