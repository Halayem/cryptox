package fr.enix.kraken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {

    MARKET("market"),
    LIMIT("limit");
    private String value;
}
