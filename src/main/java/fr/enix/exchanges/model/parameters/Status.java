package fr.enix.exchanges.model.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Status {
    OPEN    ("open" ),
    CLOSE   ("close");

    private String value;

    public static Status find(final String value){
        return Arrays.stream        (Status.values())
                     .filter        (status -> status.value.equals(value))
                     .findFirst     ()
                     .orElseThrow   (() -> new IllegalStateException(
                         String.format("this value: %s is unknown by enum: %s ", value, Status.class.getName()))
                     );
    }
}
