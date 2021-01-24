package fr.enix.exchanges.model.business;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class MarketOfferHistorySearchResponse {

    private BigDecimal price;
    private LocalDateTime dateTime;
}
