package fr.enix.exchanges.model.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetClass {
    CURRENCY("currency");

    private String value;
}
