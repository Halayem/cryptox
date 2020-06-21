package fr.enix.exchanges.model.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AddOrderType {
    BUY("buy"),
    SELL("sell");

    private String value;

    public static AddOrderType find(final String value) {
        return Arrays.stream   (AddOrderType.values())
                .filter        (addOrderType -> addOrderType.value.equals(value))
                .findFirst     ()
                .orElseThrow   (() -> new IllegalStateException(
                        String.format("this value: %s is unknown by enum: %s ", value, Status.class.getName()))
                );
    }
}
