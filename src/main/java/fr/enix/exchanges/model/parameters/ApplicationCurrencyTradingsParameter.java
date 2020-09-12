package fr.enix.exchanges.model.parameters;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ConfigurationProperties( prefix = "application.currency" )
@Getter
@Setter
public class ApplicationCurrencyTradingsParameter {

    private Map<String, TradingParameters> tradings;

    @Getter
    @Setter
    public static class TradingParameters {
        private boolean enabled;
        private List<String> strategies;
        private BigDecimal gap;
    }
}