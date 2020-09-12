package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.service.ApplicationTradingConfigurationService;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class KrakenTradingConfigurationServiceImpl implements ApplicationTradingConfigurationService {

    private final ApplicationCurrencyTradingsParameterRepository applicationTradingConfigurationRepository;
    private final CurrenciesRepresentationService currenciesRepresentationService;

    @Override
    public Flux<String> getAssetPairsToSubscribe() {
        return Flux.empty();
        /*
        return  Flux.fromIterable(
                    applicationTradingConfigurationRepository
                    .getCurrencyTradingParameters()
                    .keySet ()
                    .stream ()
                    .map    (currenciesRepresentationService::getAssetPairCurrencyRepresentationOf)
                    .collect(Collectors.toList())
                );

         */
    }
}
