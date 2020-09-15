package fr.enix.exchanges.service.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KrakenCurrenciesRepresentationServiceImplTest {

    private final KrakenCurrenciesRepresentationServiceImpl krakenCurrenciesRepresentationService = new KrakenCurrenciesRepresentationServiceImpl();

    @Test
    public void testGetAssetPairCurrencyRepresentationOf() {
        assertEquals("XLTC/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair("litecoin-euro"));
        assertEquals("XXRP/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair("ripple-euro"));
        assertEquals("XXBT/ZEUR",   krakenCurrenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair("bitcoin-euro"));
        assertEquals("SC/ZEUR",     krakenCurrenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair("siacoin-euro"));

        assertEquals("XLTC/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair("litecoin-us_dollar"));
        assertEquals("XXRP/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair("ripple-us_dollar"));
        assertEquals("XXBT/ZUSD",   krakenCurrenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair("bitcoin-us_dollar"));
        assertEquals("SC/ZUSD",     krakenCurrenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair("siacoin-us_dollar"));
    }
}
