package fr.enix.common.exception;

import fr.enix.common.exception.eapi.*;
import fr.enix.common.exception.egeneral.KrakenEgeneralInvalidArgumentsException;
import fr.enix.common.exception.eorder.KrakenEorderPositionsLimitExceededException;
import fr.enix.common.exception.eorder.KrakenEorderTradingAgreementRequiredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KrakenExceptionFactoryProviderTest {

    @Test
    public void testKrakenEorderPositionsLimitExceededException() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EOrder:Positions limit exceeded")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenEorderPositionsLimitExceededException);
        }
    }

    @Test
    public void testKrakenEorderTradingAgreementRequiredException() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EOrder:Trading agreement required")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenEorderTradingAgreementRequiredException);
        }
    }

    @Test
    public void testKrakenUnknownException_forEOrder() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EOrder:Cryptox test for unknown error")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenUnknownException);
        }
    }

    @Test
    public void testKrakenEapiRateLimitExceededException() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EAPI:Rate limit exceeded")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenEapiRateLimitExceededException);
        }
    }

    @Test
    public void testKrakenEapiInvalidKeyException() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EAPI:Invalid key")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenEapiInvalidKeyException);
        }
    }

    @Test
    public void testKrakenEapiInvalidSignatureException() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EAPI:Invalid signature")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenEapiInvalidSignatureException);
        }
    }

    @Test
    public void testKrakenEapiInvalidNonceException() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EAPI:Invalid nonce")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenEapiInvalidNonceException);
        }
    }

    @Test
    public void testKrakenEapiFeatureDisabledException() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EAPI:Feature disabled")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenEapiFeatureDisabledException);
        }
    }

    @Test
    public void testKrakenEgeneralInvalidArgumentsException() {
        assertThrows(KrakenEgeneralInvalidArgumentsException.class, () -> {
            throw KrakenExceptionFactoryProvider.getFactory         ("EGeneral:Invalid arguments:price")
                                                .getKrakenException ();
        });
    }

    @Test
    public void testKrakenUnknownException_forEAPI() {
        try {
            throw KrakenExceptionFactoryProvider.getFactory         ("EAPI:Cryptox test for unknown error")
                                                .getKrakenException ();
        } catch (KrakenException e){
            assertTrue(e instanceof KrakenUnknownException);
        }
    }
}
