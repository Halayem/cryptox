package fr.enix.common.exception;

import fr.enix.common.exception.eapi.*;
import fr.enix.common.exception.egeneral.KrakenEgeneralInvalidArgumentsException;
import fr.enix.common.exception.eorder.KrakenEorderPositionsLimitExceededException;
import fr.enix.common.exception.eorder.KrakenEorderTradingAgreementRequiredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class KrakenExceptionFactoryProviderTest {

    @Test
    void testKrakenEorderPositionsLimitExceededException() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EOrder:Positions limit exceeded").getKrakenException();
        assertThrows(KrakenEorderPositionsLimitExceededException.class, () -> { throw krakenException; });
    }

    @Test
    void testKrakenEorderTradingAgreementRequiredException() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EOrder:Trading agreement required").getKrakenException();
        assertThrows(KrakenEorderTradingAgreementRequiredException.class, () -> { throw krakenException; });
    }

    @Test
    void testKrakenUnknownException_forEOrder() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EOrder:Cryptox test for unknown error").getKrakenException();
        assertThrows(KrakenUnknownException.class, () -> { throw krakenException; });
    }

    @Test
    void testKrakenEapiRateLimitExceededException() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EAPI:Rate limit exceeded").getKrakenException();
        assertThrows(KrakenEapiRateLimitExceededException.class, () -> { throw krakenException; });
    }

    @Test
    void testKrakenEapiInvalidKeyException() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EAPI:Invalid key").getKrakenException();
        assertThrows(KrakenEapiInvalidKeyException.class, () -> { throw krakenException; });
    }

    @Test
    void testKrakenEapiInvalidSignatureException() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EAPI:Invalid signature").getKrakenException();
        assertThrows(KrakenEapiInvalidSignatureException.class, () -> { throw krakenException; });
    }

    @Test
    void testKrakenEapiInvalidNonceException() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EAPI:Invalid nonce").getKrakenException();
        assertThrows(KrakenEapiInvalidNonceException.class, () -> { throw krakenException; });
    }

    @Test
    void testKrakenEapiFeatureDisabledException() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EAPI:Feature disabled").getKrakenException();
        assertThrows(KrakenEapiFeatureDisabledException.class, () -> { throw krakenException; });
    }

    @Test
    void testKrakenEgeneralInvalidArgumentsException() {
        final KrakenException krakenException = KrakenExceptionFactoryProvider.getFactory("EGeneral:Invalid arguments:price").getKrakenException();
        assertThrows(KrakenEgeneralInvalidArgumentsException.class, () -> { throw krakenException; });
    }
}
