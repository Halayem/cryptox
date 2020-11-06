package fr.enix.exchanges.strategy.bearing;

import java.math.BigDecimal;

public interface AmountEnhancerService {

    BigDecimal getNewAmountEnhanceForSell(final String applicationAssetPair);
    BigDecimal getNewAmountEnhanceForBuy(final String applicationAssetPair);
}
