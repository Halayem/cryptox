package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.service.CurrenciesRepresentation;
import fr.enix.exchanges.service.CurrenciesRepresentationService;

public class KrakenCurrenciesRepresentationServiceImpl implements CurrenciesRepresentationService {

    @Override
    public String getCurrencyRepresentationOf(CurrenciesRepresentation currenciesRepresentation) {
        return currenciesRepresentation.getKrakenRepresentation();
    }

    @Override
    public String getAssetPairCurrencyRepresentationOf(CurrenciesRepresentation currenciesRepresentationFrom, CurrenciesRepresentation currenciesRepresentationTo) {
        return  currenciesRepresentationFrom.getKrakenRepresentation() + "/" +
                currenciesRepresentationTo.getKrakenRepresentation();
    }

    @Override
    public String getAssetPairCurrencyRepresentationOf(String assetPair) {
        return  CurrenciesRepresentation.valueOf(assetPair.split("-")[0].toUpperCase()).getKrakenRepresentation() + "/" +
                CurrenciesRepresentation.valueOf(assetPair.split("-")[1].toUpperCase()).getKrakenRepresentation();
    }
}
