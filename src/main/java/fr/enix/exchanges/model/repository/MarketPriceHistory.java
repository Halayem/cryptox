package fr.enix.exchanges.model.repository;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.ws.AssetPair;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MarketPriceHistory {

    private MarketPrice previousMarketOffer;
    private MarketPrice currentMarketOffer;

    @Getter
    @ToString
    @Builder(toBuilder = true)
    public static class MarketPrice {
        private AssetPair       assetPair;
        private BigDecimal      price;
        private LocalDateTime   date;
    }
}
