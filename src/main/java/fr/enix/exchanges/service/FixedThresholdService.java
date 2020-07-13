package fr.enix.exchanges.service;

import fr.enix.exchanges.model.repository.Threshold;
import reactor.core.publisher.Mono;

public interface FixedThresholdService {

    void updateThresholds(final fr.enix.exchanges.model.dto.Threshold threshold);
    Mono<Threshold> getThresholds();
}
