package fr.enix.exchanges.service;

public interface CurrenciesRepresentationService {

    String getCurrencyRepresentationOf(CurrenciesRepresentation currenciesRepresentation);
    String getAssetPairCurrencyRepresentationOf(CurrenciesRepresentation currenciesRepresentationFrom,
                                                CurrenciesRepresentation currenciesRepresentationTo);
    String getAssetPairCurrencyRepresentationOf(String assetPair);
}
