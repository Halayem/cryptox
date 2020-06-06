package fr.enix.exchanges.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties( prefix = "api.kraken" )
@Getter
@Setter
public class ExchangeProperties {

    private String apiKey;
    private String privateKey;
    private Map<String, String> url;

}
