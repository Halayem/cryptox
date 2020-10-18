package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.service.PriceReferenceService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TradingBearingStrategyDecisionHelper {

    private final PriceReferenceService priceReferenceService;
    private static final String UPDATED_BY = "application";

    public void updatePriceReference(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        priceReferenceService.updatePriceReference(applicationAssetPairTicker, UPDATED_BY);
    }
}
