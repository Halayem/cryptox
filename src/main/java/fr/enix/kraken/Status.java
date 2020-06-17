package fr.enix.kraken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    OPEN("open"),
    CLOSE("close");

    private String value;
}
