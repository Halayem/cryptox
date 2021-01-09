package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.parameters.ApplicationCurrencyTradingsParameter;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsStrategy;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ApplicationCurrencyTradingsParameterRepositoryImpl implements ApplicationCurrencyTradingsParameterRepository {

    private final ApplicationCurrencyTradingsParameter applicationCurrencyTradingsParameter;

    @Override
    public Flux<String> getEnabledApplicationAssetPairForTrading() {
        return  Flux.fromIterable(
                    applicationCurrencyTradingsParameter
                    .getTradings()
                    .entrySet   ()
                    .stream     ()
                    .filter     (this::isCurrencyEnabledForTrading)
                    .map        (this::getApplicationAssetPair)
                    .collect    (Collectors.toList())
        );
    }

    @Override
    public Mono<ApplicationCurrencyTradingsStrategy> getStrategyForApplicationAssetPair(final String applicationAssetPair) {
        if ( isBearingStrategyConfiguredForApplicationAssetPair     (applicationAssetPair) ) { return Mono.just(ApplicationCurrencyTradingsStrategy.STATIC_BEARING);    }
        if ( isThresholdStrategyConfiguredForApplicationAssetPair   (applicationAssetPair) ) { return Mono.just(ApplicationCurrencyTradingsStrategy.THRESHOLD);         }

        throw new RuntimeException("unhandled trading strategy for application asset pair: " + applicationAssetPair);
    }

    @Override
    public Mono<BigDecimal> getGapScaleByApplicationAssetPair(final String applicationAssetPair) {
        return Mono.justOrEmpty(
                    applicationCurrencyTradingsParameter
                        .getTradings          ()
                        .get                  (applicationAssetPair)
                        .getBearingStrategy   ()
                        .getGap               ()
                );
    }

    @Override
    public Mono<BigDecimal> getAmountToSellForBearingStrategyByApplicationAssetPair(final String applicationAssetPair) {
        return Mono.justOrEmpty(
                    applicationCurrencyTradingsParameter
                        .getTradings          ()
                        .get                  (applicationAssetPair)
                        .getBearingStrategy   ()
                        .getAmountToSell      ()
                );
    }

    @Override
    public Mono<BigDecimal> getAmountToBuyForBearingStrategyByApplicationAssetPair(final String applicationAssetPair) {
        return Mono.justOrEmpty(
                applicationCurrencyTradingsParameter
                        .getTradings          ()
                        .get                  (applicationAssetPair)
                        .getBearingStrategy   ()
                        .getAmountToBuy      ()
        );
    }

    @Override
    public BigDecimal getAmountEnhanceStepByApplicationAssetPair(final String applicationAssetPair) {
        return applicationCurrencyTradingsParameter
                .getTradings()
                .get(applicationAssetPair)
                .getBearingStrategy()
                .getAmountEnhanceStep();
    }

    private boolean isCurrencyEnabledForTrading(Map.Entry<String, ApplicationCurrencyTradingsParameter.TradingParameters> tradingParameters) {
        return tradingParameters.getValue().isEnabled();
    }

    private String getApplicationAssetPair(Map.Entry<String, ApplicationCurrencyTradingsParameter.TradingParameters> tradingParameters) {
        return tradingParameters.getKey();
    }

    private boolean isBearingStrategyConfiguredForApplicationAssetPair(final String applicationAssetPair) {
        return applicationCurrencyTradingsParameter
                .getTradings()
                .get(applicationAssetPair)
                .getBearingStrategy() != null;
    }

    private boolean isThresholdStrategyConfiguredForApplicationAssetPair(final String applicationAssetPair) {
        return applicationCurrencyTradingsParameter
                .getTradings()
                .get(applicationAssetPair)
                .getThresholdStrategy() != null;
    }
}
