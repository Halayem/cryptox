package fr.enix.exchanges.model.business;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class TickerOutput {

    private Offer ask;
    private Offer bid;
    private MarketStatus lowTrade;
    private MarketStatus highTrade;

    @Builder
    @Getter
    public static class Offer {
        private BigDecimal price;
        private BigDecimal wholeLotVolume;
        private BigDecimal lotVolume;
    }

    @Builder
    @Getter
    public static class MarketStatus {
        private BigDecimal today;
        private BigDecimal last24Hours;
    }

}
