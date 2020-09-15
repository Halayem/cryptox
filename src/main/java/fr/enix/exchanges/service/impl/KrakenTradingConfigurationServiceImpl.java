package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.service.ApplicationTradingConfigurationService;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class KrakenTradingConfigurationServiceImpl implements ApplicationTradingConfigurationService {

    private final CurrenciesRepresentationService currenciesRepresentationService;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    @Override
    public Flux<String> getEnabledAssetPairsForTrading() {
        return applicationCurrencyTradingsParameterRepository
                .getEnabledCurrenciesForTrading()
                .map(applicationCurrencyRepresentation ->
                        currenciesRepresentationService.getAssetPairCurrencyRepresentationByApplicationAssetPair(applicationCurrencyRepresentation)
                );
    }
}
