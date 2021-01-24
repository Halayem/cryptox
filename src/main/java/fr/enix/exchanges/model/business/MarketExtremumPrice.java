package fr.enix.exchanges.model.business;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class MarketExtremumPrice {

    private BigDecimal lowest;
    private BigDecimal highest;
}
