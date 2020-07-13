package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.mapper.ThresholdMapper;
import fr.enix.exchanges.model.repository.Threshold;
import fr.enix.exchanges.repository.FixedThresholdRepository;
import fr.enix.exchanges.service.FixedThresholdService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class FixedThresholdServiceImpl implements FixedThresholdService  {

    private final FixedThresholdRepository fixedThresholdRepository;
    private final ThresholdMapper thresholdMapper;

    @Override
    public void updateThresholds(final fr.enix.exchanges.model.dto.Threshold threshold) {
        fixedThresholdRepository.updateThresholds(thresholdMapper.mapFromDtoToRepository(threshold));
    }

    @Override
    public Mono<Threshold> getThresholds() {
        return fixedThresholdRepository.getThresholds();
    }
}
