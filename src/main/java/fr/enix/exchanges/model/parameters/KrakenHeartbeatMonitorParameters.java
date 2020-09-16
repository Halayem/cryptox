package fr.enix.exchanges.model.parameters;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.temporal.ChronoUnit;

@ConfigurationProperties( prefix = "kraken.heartbeat.monitor" )
@Getter
@Setter
public class KrakenHeartbeatMonitorParameters {
    private ChronoUnit timeunit;
    private long frequency;
    private long maxAge;
}
