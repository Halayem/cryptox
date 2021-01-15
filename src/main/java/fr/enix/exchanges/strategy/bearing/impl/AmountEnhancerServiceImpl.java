package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.service.ApplicationCurrencyTradingsBearingStrategy;
import fr.enix.exchanges.strategy.bearing.AmountEnhancerService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class AmountEnhancerServiceImpl implements AmountEnhancerService {

    /**
     * key is application asset pair
     */
    private Map<String, BigDecimal> computedAmountEnhance;
    private final ApplicationCurrencyTradingsBearingStrategy applicationCurrencyTradingsBearingStrategy;

    public AmountEnhancerServiceImpl(final ApplicationCurrencyTradingsBearingStrategy applicationCurrencyTradingsBearingStrategy) {
        this.computedAmountEnhance = new ConcurrentHashMap<>();
        this.applicationCurrencyTradingsBearingStrategy = applicationCurrencyTradingsBearingStrategy;
    }

    @Override
    public Mono<BigDecimal> getNewAmountEnhanceForSell(final String applicationAssetPair) {
        return updateAmountEnhance(applicationAssetPair, CurveDirection.UP);
    }

    @Override
    public Mono<BigDecimal> getNewAmountEnhanceForBuy(final String applicationAssetPair) {
        return updateAmountEnhance(applicationAssetPair, CurveDirection.DOWN);
    }

    private Mono<BigDecimal> updateAmountEnhance(final String applicationAssetPair, final CurveDirection curveDirection) {

        return
            amountEnhanceRedux(
                curveDirection,
                getCurrentAmountEnhance ( applicationAssetPair ),
                getStepAmountEnhance    ( applicationAssetPair )
            ).map( amountEnhanceReduced -> {
                log.debug("application asset pair: {} computed new amount enhance: {} for curve direction: {}", applicationAssetPair, amountEnhanceReduced.abs(), curveDirection.toString());
                computedAmountEnhance.put( applicationAssetPair, amountEnhanceReduced );
                return computedAmountEnhance.get(applicationAssetPair).abs();
            });
    }

    private Mono<BigDecimal> getStepAmountEnhance(final String applicationAssetPair) {
        return  applicationCurrencyTradingsBearingStrategy
                .getApplicationCurrencyTradingsBearingStrategyService(applicationAssetPair)
                .flatMap(applicationCurrencyTradingsBearingStrategyService -> applicationCurrencyTradingsBearingStrategyService.getAmountEnhanceStepByApplicationAssetPair(applicationAssetPair));
    }

    private Mono<BigDecimal> amountEnhanceRedux(final CurveDirection curveDirection, final BigDecimal currentAmountEnhance, final Mono<BigDecimal> stepAmountEnhancer) {
        if( currentAmountEnhance == null ) {
            return Mono.just(BigDecimal.ZERO);
        }

        return stepAmountEnhancer.map( amountEnhancer -> {
            switch (curveDirection) {
                case UP:    return currentAmountEnhance.compareTo(BigDecimal.ZERO) >= 0 ? currentAmountEnhance.add(amountEnhancer)      : BigDecimal.ZERO;
                case DOWN:  return currentAmountEnhance.compareTo(BigDecimal.ZERO) <= 0 ? currentAmountEnhance.subtract(amountEnhancer) : BigDecimal.ZERO;
                default:    throw new IllegalArgumentException("unhandled curve direction: " + curveDirection.toString());
            }
        });
    }

    private BigDecimal getCurrentAmountEnhance(final String applicationAssetPair) {
        return computedAmountEnhance.get(applicationAssetPair);
    }

    protected void resetAllComputedAmountEnhance() {
        computedAmountEnhance = new ConcurrentHashMap<>();
    }
    private enum CurveDirection {
        UP, DOWN
    }
}
