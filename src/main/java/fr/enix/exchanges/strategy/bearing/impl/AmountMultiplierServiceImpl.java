package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.strategy.bearing.AmountMultiplierService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Slf4j
public class AmountMultiplierServiceImpl implements AmountMultiplierService {

    /**
     * key is application asset pair
     */
    private final Map<String, Integer> amountMultiplier = new HashMap<>();

    @Override
    public Integer getNewAmountMultiplierForSell(final String applicationAssetPair) {
        updateAmountMultiplier(applicationAssetPair, CurveDirection.UP);
        return amountMultiplier.get(applicationAssetPair);
    }

    @Override
    public Integer getNewAmountMultiplierForBuy(final String applicationAssetPair) {
        updateAmountMultiplier(applicationAssetPair, CurveDirection.DOWN);
        return Math.abs( amountMultiplier.get(applicationAssetPair) );
    }

    private void updateAmountMultiplier(final String applicationAssetPair, final CurveDirection curveDirection) {
        amountMultiplier.put( applicationAssetPair, amountMultiplierRedux( curveDirection, getCurrentAmountMultiplier( applicationAssetPair ) ) );
        log.info("application asset pair: {} has new amount multiplier: {} for curve direction: {}", applicationAssetPair, amountMultiplier.get(applicationAssetPair), curveDirection.toString());
    }

    private Integer amountMultiplierRedux(final CurveDirection curveDirection, final Integer currentAmountMultiplier) {
        switch (curveDirection) {
            case UP:    return currentAmountMultiplier >= 0 ? currentAmountMultiplier + 1 : 1;
            case DOWN:  return currentAmountMultiplier <= 0 ? currentAmountMultiplier - 1 : -1;
            default:    throw new IllegalArgumentException("unhandled curve direction: " + curveDirection.toString());
        }

    }

    private Integer getCurrentAmountMultiplier(final String applicationAssetPair) {
        return amountMultiplier.containsKey(applicationAssetPair) ? amountMultiplier.get(applicationAssetPair) : 0;
    }

    private enum CurveDirection {
        UP, DOWN
    }
}
