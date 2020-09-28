package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.parameters.ApplicationCurrencyTradingsParameter;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
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
    public Flux<String> getStrategiesByApplicationAssetPair(final String applicationAssetPair) {
        List<String> strategies = new ArrayList();
        if ( isBearingStrategyConfiguredForApplicationAssetPair     (applicationAssetPair) ) { strategies.add("bearing");   }
        if ( isThresholdStrategyConfiguredForApplicationAssetPair   (applicationAssetPair) ) { strategies.add("threshold"); }

        return Flux.fromIterable(strategies);
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
