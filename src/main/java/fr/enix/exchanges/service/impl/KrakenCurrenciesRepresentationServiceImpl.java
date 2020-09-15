package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.service.CurrenciesRepresentation;
import fr.enix.exchanges.service.CurrenciesRepresentationService;

public class KrakenCurrenciesRepresentationServiceImpl implements CurrenciesRepresentationService {

    @Override
    public String getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair(final String applicationAssetPair) {
        return  CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[0].toUpperCase()).getKrakenWebServiceRepresentation() + "/" +
                CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[1].toUpperCase()).getKrakenWebServiceRepresentation();
    }

    @Override
    public String getAssetPairCurrencyWebSocketRepresentationByApplicationAssetPair(String applicationAssetPair) {
        return  CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[0].toUpperCase()).getKrakenWebSocketRepresentation() + "/" +
                CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[1].toUpperCase()).getKrakenWebSocketRepresentation();
    }
}
