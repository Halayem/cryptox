package fr.enix.exchanges.service.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KrakenCurrenciesRepresentationServiceImplTest {

    private final KrakenCurrenciesRepresentationServiceImpl krakenCurrenciesRepresentationService = new KrakenCurrenciesRepresentationServiceImpl();

    @Test
    void testGetAssetPairCurrencyRepresentationOf() {
        assertEquals("XLTC/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("litecoin-euro"));
        assertEquals("XXRP/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("ripple-euro"));
        assertEquals("XXBT/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("bitcoin-euro"));
        assertEquals("SC/ZEUR",     krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("siacoin-euro"));

        assertEquals("XLTC/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("litecoin-us_dollar"));
        assertEquals("XXRP/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("ripple-us_dollar"));
        assertEquals("XXBT/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("bitcoin-us_dollar"));
        assertEquals("SC/ZUSD",     krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("siacoin-us_dollar"));
    }

    @Test
    void testGetApplicationAssetPairCurrencyRepresentationByMarketAssetPair() {
        assertEquals("litecoin-euro",   krakenCurrenciesRepresentationService.getApplicationAssetPairCurrencyRepresentationByMarketAssetPair("LTC/EUR"));
        assertEquals("siacoin-euro",    krakenCurrenciesRepresentationService.getApplicationAssetPairCurrencyRepresentationByMarketAssetPair("SC/EUR"));
        assertEquals("bitcoin-euro",    krakenCurrenciesRepresentationService.getApplicationAssetPairCurrencyRepresentationByMarketAssetPair("XBT/EUR"));
        assertEquals("ripple-euro",     krakenCurrenciesRepresentationService.getApplicationAssetPairCurrencyRepresentationByMarketAssetPair("XRP/EUR"));
    }

    @Test
    void testGetApplicationAssetPairCurrencyRepresentationByMarketAssetPair_shouldThrowExceptionWhenFirstMarketAssetIsUnknown() {
        Exception exception = assertThrows(
                IllegalStateException.class, () ->
                        krakenCurrenciesRepresentationService.getApplicationAssetPairCurrencyRepresentationByMarketAssetPair("XYZ/EUR")
                );

        assertEquals("unknown kraken market asset: XYZ", exception.getMessage());
    }

    @Test
    void testGetApplicationAssetPairCurrencyRepresentationByMarketAssetPair_shouldThrowExceptionWhenSecondMarketAssetIsUnknown() {
        Exception exception = assertThrows(
                IllegalStateException.class, () ->
                        krakenCurrenciesRepresentationService.getApplicationAssetPairCurrencyRepresentationByMarketAssetPair("LTC/ABC")
        );

        assertEquals("unknown kraken market asset: ABC", exception.getMessage());
    }
}
