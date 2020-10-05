package fr.enix.exchanges.service.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KrakenCurrenciesRepresentationServiceImplTest {

    private final KrakenCurrenciesRepresentationServiceImpl krakenCurrenciesRepresentationService = new KrakenCurrenciesRepresentationServiceImpl();

    @Test
    void testGetAssetPairCurrencyRepresentationOf() {
        assertEquals("XLTCZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("litecoin-euro"));
        assertEquals("XXRPZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("ripple-euro"));
        assertEquals("XXBTZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("bitcoin-euro"));
        assertEquals("SCZEUR",     krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("siacoin-euro"));

        assertEquals("XLTCZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("litecoin-us_dollar"));
        assertEquals("XXRPZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("ripple-us_dollar"));
        assertEquals("XXBTZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("bitcoin-us_dollar"));
        assertEquals("SCZUSD",     krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("siacoin-us_dollar"));
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
