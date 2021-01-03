package fr.enix.exchanges.model.repository;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
// @TODO to delete
public class ApplicationAssetPairTicker implements Comparable<ApplicationAssetPairTicker> {
    private String          applicationAssetPair;
    private BigDecimal      price;
    private LocalDateTime   dateTime;

    @Override
    public int compareTo(ApplicationAssetPairTicker applicationAssetPairMarketTicker) {
        return this.getDateTime().compareTo(applicationAssetPairMarketTicker.getDateTime());
    }
}