package fr.enix.exchanges.model.parameters;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "kraken.ping" )
@Getter
@Setter
public class KrakenPingProperties {
    private String payload;
    private long frequency;
}
