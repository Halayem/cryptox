package fr.enix.common.exception;

import fr.enix.common.exception.eapi.KrakenEapiExceptionFactory;
import fr.enix.common.exception.egeneral.KrakenEgeneralExceptionFactory;
import fr.enix.common.exception.eorder.KrakenEorderExceptionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KrakenExceptionFactoryProvider {

    private static final String EORDER_MESSAGE_PREFIX       = "EOrder:";
    private static final String EAPI_MESSAGE_PREFIX         = "EAPI:";
    private static final String EGENERAL_MESSAGE_PREFIX     = "EGeneral:";

    public static KrakenExceptionAbstractFactory getFactory(final String message) {
        if ( message.startsWith(EORDER_MESSAGE_PREFIX   )) return new KrakenEorderExceptionFactory  (message.replace(EORDER_MESSAGE_PREFIX,     ""));
        if ( message.startsWith(EAPI_MESSAGE_PREFIX     )) return new KrakenEapiExceptionFactory    (message.replace(EAPI_MESSAGE_PREFIX,       ""));
        if ( message.startsWith(EGENERAL_MESSAGE_PREFIX )) return new KrakenEgeneralExceptionFactory(message.replace(EGENERAL_MESSAGE_PREFIX,   ""));
        return null;
    }
}
