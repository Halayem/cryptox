package fr.enix.common.exception;

import lombok.Getter;

@Getter
public class FactoryException extends RuntimeException {

    private final String message;

    public FactoryException(final String message) {
        super(message);
        this.message = message;
    }
}
