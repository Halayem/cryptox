package fr.enix.exchanges.model.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {

    MARKET                  ("market"),
    LIMIT                   ("limit"),
    STOP_LOSS_PROFIT        ("stop-loss-profit"),
    STOP_LOSS               ("stop-loss"),
    STOP_LOSS_PROFIT_LIMIT  ("stop-loss-profit-limit");

    private String value;
}
