package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.service.CurrenciesRepresentation;
import fr.enix.exchanges.service.CurrenciesRepresentationService;

import java.util.Arrays;

public class KrakenCurrenciesRepresentationServiceImpl implements CurrenciesRepresentationService {

    @Override
    public String getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair(final String applicationAssetPair) {
        return  CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[0].toUpperCase()).getKrakenWebServiceRepresentation()+
                CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[1].toUpperCase()).getKrakenWebServiceRepresentation();
    }

    @Override
    public String getAssetPairCurrencyWebSocketRepresentationByApplicationAssetPair(final String applicationAssetPair) {
        return  CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[0].toUpperCase()).getKrakenWebSocketRepresentation() + "/" +
                CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[1].toUpperCase()).getKrakenWebSocketRepresentation();
    }

    @Override
    public String getApplicationAssetPairCurrencyRepresentationByMarketAssetPair(final String marketAssetPair) {

        return getApplicationAssetCurrencyRepresentationByMarketAsset(marketAssetPair.split("/")[0].toUpperCase())
                .concat("-")
                .concat(
                        getApplicationAssetCurrencyRepresentationByMarketAsset(marketAssetPair.split("/")[1].toUpperCase())
                );
    }

    private String getApplicationAssetCurrencyRepresentationByMarketAsset(final String marketAsset) {
        return
                Arrays
                .stream     (CurrenciesRepresentation.values())
                .filter     (currenciesRepresentation -> currenciesRepresentation.getKrakenWebSocketRepresentation().equals(marketAsset))
                .findFirst  ()
                .orElseThrow(() -> new IllegalStateException(String.format("unknown kraken market asset: %s", marketAsset)))
                .toString   ()
                .toLowerCase();
    }

}
