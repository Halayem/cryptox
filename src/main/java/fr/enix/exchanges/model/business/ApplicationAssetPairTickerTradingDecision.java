package fr.enix.exchanges.model.business;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class ApplicationAssetPairTickerTradingDecision {

    // used as a reference for taking decision
    private final ApplicationAssetPairTicker applicationAssetPairTickerReference;
    private final Operation     operation;
    private final BigDecimal    amount;
    private final BigDecimal    price;

    @Getter
    @Builder
    public static class Operation {
        private final Decision  decision;
        private final String    message;
    }

    public enum Decision {
        BUY, SELL, DO_NOTHING, ERROR
    }
}
