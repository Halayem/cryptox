package fr.enix.exchanges.model.business.output;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@ToString
public class TickerOutput {

    private Offer ask;
    private Offer bid;
    private MarketStatus lowTrade;
    private MarketStatus highTrade;
    private String assetPair;

    @Builder
    @Getter
    @ToString
    public static class Offer {
        private BigDecimal price;
        private BigDecimal wholeLotVolume;
        private BigDecimal lotVolume;
    }

    @Builder
    @Getter
    @ToString
    public static class MarketStatus {
        private BigDecimal today;
        private BigDecimal last24Hours;
    }

}
