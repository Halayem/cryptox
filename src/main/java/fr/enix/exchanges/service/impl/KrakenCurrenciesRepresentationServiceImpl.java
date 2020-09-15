package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.service.CurrenciesRepresentation;
import fr.enix.exchanges.service.CurrenciesRepresentationService;

public class KrakenCurrenciesRepresentationServiceImpl implements CurrenciesRepresentationService {

    @Override
    public String getAssetPairCurrencyRepresentationByApplicationAssetPair(final String applicationAssetPair) {
        return  CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[0].toUpperCase()).getKrakenRepresentation() + "/" +
                CurrenciesRepresentation.valueOf(applicationAssetPair.split("-")[1].toUpperCase()).getKrakenRepresentation();
    }
}
