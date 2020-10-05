package fr.enix.exchanges.service;

public interface CurrenciesRepresentationService {

    String getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair(final String applicationAssetPair);
    String getAssetPairCurrencyWebSocketRepresentationByApplicationAssetPair(final String applicationAssetPair);
    String getAssetPairCurrencyWithoutPrefixAndWithoutSeparatorRepresentationByApplicationAssetPair(final String applicationAssetPair);
    String getApplicationAssetPairCurrencyRepresentationByMarketAssetPair(final String marketAssetPair);
}
