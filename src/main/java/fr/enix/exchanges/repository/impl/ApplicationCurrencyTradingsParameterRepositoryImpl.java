package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.parameters.ApplicationCurrencyTradingsParameter;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
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
        return Flux.fromIterable(
                applicationCurrencyTradingsParameter
                        .getTradings    ()
                        .get            (applicationAssetPair)
                        .getStrategies  ()
                );
    }

    public Mono<BigDecimal> getPriceGapByApplicationAssetPair(final String applicationAssetPair) {
        return Mono.justOrEmpty(
                applicationCurrencyTradingsParameter
                        .getTradings()
                        .get        (applicationAssetPair)
                        .getGap     ()
                );
    }
}
