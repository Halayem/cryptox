package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.repository.Threshold;
import fr.enix.exchanges.repository.FixedThresholdRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class FixedThresholdRepositoryImpl implements FixedThresholdRepository {

    private Threshold threshold;

    public FixedThresholdRepositoryImpl(final Threshold threshold) {
        this.threshold = threshold;
        log.info("initial thresholds: {}", threshold);
    }

    @Override
    public void updateThresholds(final Threshold threshold) {
        this.threshold = threshold;
        log.info("updated thresholds: {}", threshold);
    }

    public Mono<Threshold> getThresholds() {
        return Mono.just(threshold);
    }
}
