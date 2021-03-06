package fr.enix.exchanges.model.parameters;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
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
        private StaticBearingStrategy staticBearingStrategy;
        private ThresholdStrategy thresholdStrategy;
    }

    @Getter
    @Setter
    public static class StaticBearingStrategy {
        private BigDecimal gap;
        private BigDecimal amountToSell;
        private BigDecimal amountToBuy;
        private BigDecimal amountEnhanceStep;
        private BigDecimal buyStopLoss;
    }

    @Getter
    @Setter
    public static class ThresholdStrategy {
        private BigDecimal triggerPriceToBuy;
        private BigDecimal amountToBuy;
        private BigDecimal triggerPriceToSell;
        private BigDecimal amountToSell;
    }
}