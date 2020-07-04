package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.repository.MarketPriceHistory;
import reactor.core.publisher.Mono;

public interface TransactionDecisionService {

    Mono<Decision> getDecision(final MarketPriceHistory marketPriceHistory);
}
