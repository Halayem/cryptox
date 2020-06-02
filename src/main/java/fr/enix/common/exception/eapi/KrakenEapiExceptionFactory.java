package fr.enix.common.exception.eapi;

import fr.enix.common.exception.KrakenException;
import fr.enix.common.exception.KrakenExceptionAbstractFactory;
import fr.enix.common.exception.KrakenUnknownException;

public class KrakenEapiExceptionFactory extends KrakenExceptionAbstractFactory  {

    private final String message;

    public KrakenEapiExceptionFactory(final String message) {
        this.message = message;
    }

    @Override
    public KrakenException getKrakenException() {
        switch (message) {
            case "Rate limit exceeded": return new KrakenEapiRateLimitExceededException();
            case "Invalid key":         return new KrakenEapiInvalidKeyException();
            case "Invalid signature":   return new KrakenEapiInvalidSignatureException();
            case "Invalid nonce":       return new KrakenEapiInvalidNonceException();
            case "Feature disabled":    return new KrakenEapiFeatureDisabledException();
            default:                    return new KrakenUnknownException();
        }

    }
}
