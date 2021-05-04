package fr.enix.exchanges.model.business;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
public class ApplicationAssetPairTickerTradingDecision {

    // used as a reference for taking decision
    private final ApplicationAssetPairTicker applicationAssetPairTickerReference;
    private final Operation     operation;
    private final BigDecimal    amount;
    private final BigDecimal    price;
    private final BigDecimal    stopLossPrice;

    @Getter
    @Builder
    public static class Operation {
        private final Decision  decision;
        private final String    message;
    }

    public enum Decision {
        BUY, BUY_STOP_LOSS, SELL, DO_NOTHING, ERROR
    }

    public String getFormattedLogMessage() {
        return
            String.format(
                "\n\t - application asset pair -- <%s>" +
                "\n\t - current price ----------- <%f>" +
                "\n\t - decision ---------------- <%s>" +
                "\n\t - message ----------------- <%s>" +
                "\n\t - transaction amount ------ <%f>" +
                "\n\t - transaction price ------- <%f>" +
                "\n\t - buy stop loss price ----- <%f>",
                applicationAssetPairTickerReference.getApplicationAssetPair(),
                applicationAssetPairTickerReference.getPrice(),
                operation.getDecision(),
                operation.getMessage(),
                amount,
                price,
                stopLossPrice);
    }
}
