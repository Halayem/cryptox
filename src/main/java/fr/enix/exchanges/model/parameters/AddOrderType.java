package fr.enix.exchanges.model.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddOrderType {
    BUY("buy"),
    SELL("sell");

    private String value;
}
