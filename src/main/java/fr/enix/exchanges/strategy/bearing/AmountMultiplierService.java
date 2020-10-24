package fr.enix.exchanges.strategy.bearing;

public interface AmountMultiplierService {

    Integer getNewAmountMultiplierForSell(final String applicationAssetPair);
    Integer getNewAmountMultiplierForBuy(final String applicationAssetPair);
}
