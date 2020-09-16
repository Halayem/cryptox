package fr.enix.exchanges.configuration.monitor;

import fr.enix.exchanges.model.parameters.KrakenHeartbeatMonitorParameters;
import fr.enix.exchanges.monitor.HeartbeatMonitor;
import fr.enix.exchanges.monitor.impl.KrakenHeartbeatMonitorImpl;
import fr.enix.exchanges.repository.HeartbeatRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KrakenHeartbeatMonitorParameters.class)
public class MonitorConfiguration {

    @Bean
    public HeartbeatMonitor heartbeatMonitor(final HeartbeatRepository heartbeatRepository,
                                             final KrakenHeartbeatMonitorParameters krakenHeartbeatMonitorParameters) {
        return new KrakenHeartbeatMonitorImpl(heartbeatRepository, krakenHeartbeatMonitorParameters);
    }
}
