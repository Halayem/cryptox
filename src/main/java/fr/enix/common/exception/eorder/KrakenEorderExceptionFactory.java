package fr.enix.common.exception.eorder;

import fr.enix.common.exception.KrakenException;
import fr.enix.common.exception.KrakenExceptionAbstractFactory;
import fr.enix.common.exception.KrakenUnknownException;

public class KrakenEorderExceptionFactory extends KrakenExceptionAbstractFactory {

    private final String message;

    public KrakenEorderExceptionFactory(final String message) {
        this.message = message;
    }

    @Override
    public KrakenException getKrakenException() {
        switch (message) {
            case "Positions limit exceeded":    return new KrakenEorderPositionsLimitExceededException();
            case "Trading agreement required":  return new KrakenEorderTradingAgreementRequiredException();
            case "Insufficient funds":          return new KrakenEorderInsufficientFundsException();
            default:                            return new KrakenUnknownException();
        }
    }
}
