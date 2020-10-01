package fr.enix.exchanges.model.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AddOrderType {
    BUY ("buy"),
    SELL("sell");

    private String krakenOrderType;

}
