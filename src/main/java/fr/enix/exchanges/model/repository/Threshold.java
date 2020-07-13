package fr.enix.exchanges.model.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class Threshold {

    private BigDecimal thresholdToBuy;
    private BigDecimal thresholdToSell;
}
