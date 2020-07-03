package fr.enix.exchanges.model.repository;

import fr.enix.exchanges.model.parameters.Asset;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketPriceHistory {

    private MarketPrice previousMarketPrice;
    private MarketPrice currentMarketPrice;

    @Getter

    @Builder(toBuilder = true)
    public static class MarketPrice {
        private Asset           asset;
        private BigDecimal      price;
        private LocalDateTime   date;
    }
}
