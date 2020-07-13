package fr.enix.exchanges.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties( prefix = "cryptox.threshold.litecoin.default" )
@Getter
@Setter
public class FixedThresholdProperties {

    private BigDecimal buy;
    private BigDecimal sell;
}
