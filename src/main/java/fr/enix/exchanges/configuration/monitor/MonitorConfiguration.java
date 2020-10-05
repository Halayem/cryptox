package fr.enix.exchanges.configuration.monitor;

import fr.enix.exchanges.monitor.impl.ApplicationPongMonitorImpl;
import fr.enix.exchanges.repository.ApplicationMonitoringParametersRepository;
import fr.enix.exchanges.monitor.ApplicationMonitor;
import fr.enix.exchanges.monitor.impl.ApplicationHeartbeatMonitorImpl;
import fr.enix.exchanges.repository.HeartbeatRepository;
import fr.enix.exchanges.repository.PongRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApplicationMonitoringParametersRepository.class)
public class MonitorConfiguration {

    @Bean
    public ApplicationMonitor heartbeatMonitor(final HeartbeatRepository heartbeatRepository,
                                               final ApplicationMonitoringParametersRepository applicationMonitoringParametersRepository) {
        return new ApplicationHeartbeatMonitorImpl(heartbeatRepository, applicationMonitoringParametersRepository);
    }

    @Bean
    public ApplicationMonitor pongMonitor(final PongRepository pongRepository,
                                          final ApplicationMonitoringParametersRepository applicationMonitoringParametersRepository) {
        return new ApplicationPongMonitorImpl(pongRepository, applicationMonitoringParametersRepository);
    }
}
