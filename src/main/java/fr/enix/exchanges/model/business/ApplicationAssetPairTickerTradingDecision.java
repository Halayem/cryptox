package fr.enix.exchanges.model.business;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ApplicationAssetPairTickerTradingDecision {

    private final Decision decision;
    private final ApplicationAssetPairTicker applicationAssetPairTicker;

    public enum Decision {
        BUY, SELL, DO_NOTHING, ERROR
    }
}
