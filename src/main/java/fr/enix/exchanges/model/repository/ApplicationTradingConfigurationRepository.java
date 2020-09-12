package fr.enix.exchanges.model.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ConfigurationProperties( prefix = "application.trading" )
@Getter
@Setter
public class ApplicationTradingConfigurationRepository {

    private Map<String, TradingParameters> currencyTradingParameters;

    @Getter
    @Setter
    private static class TradingParameters {
        private boolean enabled;
        private List<String> strategies;
        private BigDecimal gap;
    }

}
