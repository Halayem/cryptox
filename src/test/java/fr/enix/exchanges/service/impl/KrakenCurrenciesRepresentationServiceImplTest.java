package fr.enix.exchanges.service.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KrakenCurrenciesRepresentationServiceImplTest {

    private final KrakenCurrenciesRepresentationServiceImpl krakenCurrenciesRepresentationService = new KrakenCurrenciesRepresentationServiceImpl();

    @Test
    public void testGetAssetPairCurrencyRepresentationOf() {
        assertEquals("XLTC/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("litecoin-euro"));
        assertEquals("XXRP/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("ripple-euro"));
        assertEquals("XXBT/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("bitcoin-euro"));
        assertEquals("SC/ZEUR",     krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("siacoin-euro"));

        assertEquals("XLTC/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("litecoin-us_dollar"));
        assertEquals("XXRP/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("ripple-us_dollar"));
        assertEquals("XXBT/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("bitcoin-us_dollar"));
        assertEquals("SC/ZUSD",     krakenCurrenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair("siacoin-us_dollar"));
    }
}
