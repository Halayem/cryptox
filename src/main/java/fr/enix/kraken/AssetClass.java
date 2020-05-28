package fr.enix.kraken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetClass {
    CURRENCY("currency");

    private String value;
}
