package fr.enix.exchanges.service;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.repository.Decision;
import reactor.core.publisher.Mono;

public interface TransactionDecisionService {

    Mono<Decision> getDecision(final Asset asset);
}
