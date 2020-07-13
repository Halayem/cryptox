package fr.enix.exchanges.configuration.repository;

import fr.enix.exchanges.model.FixedThresholdProperties;
import fr.enix.exchanges.model.repository.Threshold;
import fr.enix.exchanges.repository.FixedThresholdRepository;
import fr.enix.exchanges.repository.impl.FixedThresholdRepositoryImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FixedThresholdProperties.class)
public class FixedThresholdRepositoryConfiguration {

    @Bean
    public FixedThresholdRepository fixedThresholdRepository(final FixedThresholdProperties fixedThresholdProperties) {
        return new FixedThresholdRepositoryImpl(
                Threshold.builder()
                         .thresholdToBuy(fixedThresholdProperties.getBuy())
                         .thresholdToSell(fixedThresholdProperties.getSell())
                         .build()
        );
    }
}
