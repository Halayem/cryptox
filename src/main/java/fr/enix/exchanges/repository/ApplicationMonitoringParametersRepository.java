package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.MonitoringConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties( prefix = "application.monitoring" )
@Getter
@Setter
public class ApplicationMonitoringParametersRepository {

    private Map<String, MonitoringConfiguration> events;

    public MonitoringConfiguration getMonitoringConfigurationForEvent(String event) {
        if( !events.containsKey(event)) {
            throw new RuntimeException("unknown event: " + event);
        }
        return events.get(event);
    }

}
