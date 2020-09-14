package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.parameters.ApplicationCurrencyTradingsParameter;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ApplicationCurrencyTradingsParameterRepositoryImpl implements ApplicationCurrencyTradingsParameterRepository {

    private final ApplicationCurrencyTradingsParameter applicationCurrencyTradingsParameter;

    public Flux<String> getEnabledCurrenciesForTrading() {
        return  Flux.fromIterable(
                    applicationCurrencyTradingsParameter
                    .getTradings()
                    .entrySet   ()
                    .stream     ()
                    .filter     (entry -> entry.getValue().isEnabled())
                    .map        (entry -> entry.getKey())
                    .collect    (Collectors.toList())
        );
    }

    public Flux<String> getStrategiesByApplicationAssetPair(final String applicationAssetPair) {
        List<String> strategies = new ArrayList();
        if ( isBearingStrategyConfiguredForApplicationAssetPair     (applicationAssetPair) ) { strategies.add("bearing");   }
        if ( isThresholdStrategyConfiguredForApplicationAssetPair   (applicationAssetPair) ) { strategies.add("threshold"); }

        return Flux.fromIterable(strategies);
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