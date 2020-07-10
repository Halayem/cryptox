package fr.enix.common.exception.egeneral;

import fr.enix.common.exception.KrakenException;
import fr.enix.common.exception.KrakenExceptionAbstractFactory;
import fr.enix.common.exception.KrakenUnknownException;

public class KrakenEgeneralExceptionFactory extends KrakenExceptionAbstractFactory {

    private final String message;

    public KrakenEgeneralExceptionFactory(final String message) {
        this.message = message;
    }

    @Override
    public KrakenException getKrakenException() {
        if (message.startsWith("Invalid arguments")) {
            return new KrakenEgeneralInvalidArgumentsException();
        }
        return new KrakenUnknownException();
    }
}
