package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.Threshold;
import reactor.core.publisher.Mono;

public interface FixedThresholdRepository {

    void updateThresholds(final Threshold threshold);
    Mono<Threshold> getThresholds();
}
