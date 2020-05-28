package fr.enix.kraken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddOrderType {
    BUY("buy"),
    SELL("sell");

    private String value;
}
