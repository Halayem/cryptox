package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.ApplicationTradingConfigurationRepository;
import fr.enix.exchanges.service.ApplicationTradingConfigurationService;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@AllArgsConstructor
public class KrakenTradingConfigurationServiceImpl implements ApplicationTradingConfigurationService {

    private final ApplicationTradingConfigurationRepository applicationTradingConfigurationRepository;
    private final CurrenciesRepresentationService currenciesRepresentationService;

    @Override
    public Flux<String> getAssetPairsToSubscribe() {
        return  Flux.fromIterable(
                    applicationTradingConfigurationRepository
                    .getCurrencyTradingParameters()
                    .keySet ()
                    .stream ()
                    .map    (currenciesRepresentationService::getAssetPairCurrencyRepresentationOf)
                    .collect(Collectors.toList())
                );
    }
}
