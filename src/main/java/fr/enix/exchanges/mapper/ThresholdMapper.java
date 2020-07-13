package fr.enix.exchanges.mapper;

import fr.enix.exchanges.model.repository.Threshold;

public class ThresholdMapper {

    public Threshold mapFromDtoToRepository(final fr.enix.exchanges.model.dto.Threshold threshold) {
        return Threshold.builder()
                        .thresholdToSell(threshold.getThresholdToSell())
                        .thresholdToBuy(threshold.getThresholdToBuy())
                        .build();
    }

    public fr.enix.exchanges.model.dto.Threshold mapFromRepositoryToDto(final Threshold threshold) {
        return fr.enix.exchanges.model.dto.Threshold.builder()
                                                    .thresholdToSell(threshold.getThresholdToSell())
                                                    .thresholdToBuy(threshold.getThresholdToBuy())
                                                    .build();
    }
}
