package fr.enix.exchanges.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix = "api.kraken" )
@Getter
@Setter
public class ExchangeProperties {

    private String apiKey;
    private String privateKey;
    private String url;

}
