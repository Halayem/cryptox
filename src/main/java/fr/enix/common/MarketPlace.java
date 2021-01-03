package fr.enix.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MarketPlace {

    KRAKEN("kraken"),
    HITBTC("hitbtc");

    private String value;
}
