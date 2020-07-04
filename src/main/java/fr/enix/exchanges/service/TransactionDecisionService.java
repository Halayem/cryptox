package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.ws.AssetPair;
import reactor.core.publisher.Mono;

public interface TransactionDecisionService {

    Mono<Decision> getDecision(final AssetPair assetPair);
}
