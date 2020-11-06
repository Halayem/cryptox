package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.strategy.bearing.AmountEnhancerService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AmountEnhancerServiceImpl implements AmountEnhancerService {

    /**
     * key is application asset pair
     */
    private final Map<String, BigDecimal> computedAmountEnhance;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    public AmountEnhancerServiceImpl(final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository) {
        this.computedAmountEnhance = new HashMap<>();
        this.applicationCurrencyTradingsParameterRepository = applicationCurrencyTradingsParameterRepository;
    }

    @Override
    public BigDecimal getNewAmountEnhanceForSell(final String applicationAssetPair) {
        updateAmountEnhance(applicationAssetPair, CurveDirection.UP);
        return computedAmountEnhance.get(applicationAssetPair);
    }

    @Override
    public BigDecimal getNewAmountEnhanceForBuy(final String applicationAssetPair) {
        updateAmountEnhance(applicationAssetPair, CurveDirection.DOWN);
        return computedAmountEnhance.get(applicationAssetPair).abs();
    }

    private void updateAmountEnhance(final String applicationAssetPair, final CurveDirection curveDirection) {
        computedAmountEnhance.put(
            applicationAssetPair,
            amountEnhanceRedux(
                curveDirection,
                getCurrentAmountEnhance( applicationAssetPair ),
                applicationCurrencyTradingsParameterRepository.getAmountEnhanceStepByApplicationAssetPair(applicationAssetPair)
            )
        );
        log.info("application asset pair: {} has new amount enhance: {} for curve direction: {}", applicationAssetPair, computedAmountEnhance.get(applicationAssetPair).abs(), curveDirection.toString());
    }

    private BigDecimal amountEnhanceRedux(final CurveDirection curveDirection, final BigDecimal currentAmountEnhance, final BigDecimal stepAmountEnhancer) {
        if( currentAmountEnhance == null ) {
            return BigDecimal.ZERO;
        }
        switch (curveDirection) {
            case UP:    return currentAmountEnhance.compareTo(BigDecimal.ZERO) >= 0 ? currentAmountEnhance.add      (stepAmountEnhancer) : BigDecimal.ZERO;
            case DOWN:  return currentAmountEnhance.compareTo(BigDecimal.ZERO) <= 0 ? currentAmountEnhance.subtract (stepAmountEnhancer) : BigDecimal.ZERO;
            default:    throw new IllegalArgumentException("unhandled curve direction: " + curveDirection.toString());
        }

    }

    private BigDecimal getCurrentAmountEnhance(final String applicationAssetPair) {
        return computedAmountEnhance.get(applicationAssetPair);
    }

    private enum CurveDirection {
        UP, DOWN
    }
}
