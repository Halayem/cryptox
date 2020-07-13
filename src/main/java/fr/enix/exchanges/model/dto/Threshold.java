package fr.enix.exchanges.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Threshold {

    private BigDecimal thresholdToBuy;
    private BigDecimal thresholdToSell;
}
