package fr.enix.exchanges.service;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.service.impl.ApplicationCurrencyTradingsDynamicBearingStrategyServiceImpl;
import fr.enix.exchanges.service.impl.ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ApplicationCurrencyTradingsBearingStrategy {

    private final ApplicationCurrencyTradingsDynamicBearingStrategyServiceImpl  applicationCurrencyTradingsDynamicBearingStrategyService;
    private final ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl   applicationCurrencyTradingsStaticBearingStrategyService;
    private final ApplicationCurrencyTradingsParameterRepository                applicationCurrencyTradingsParameterRepository;

    public Mono<ApplicationCurrencyTradingsBearingStrategyService> getApplicationCurrencyTradingsBearingStrategyService(final String applicationAssetPair) {
        return  applicationCurrencyTradingsParameterRepository
                .getStrategyForApplicationAssetPair ( applicationAssetPair )
                .map                                ( applicationCurrencyTradingsStrategy -> {
                    switch (applicationCurrencyTradingsStrategy) {
                        case STATIC_BEARING:    return applicationCurrencyTradingsStaticBearingStrategyService;
                        case DYNAMIC_BEARING:   return applicationCurrencyTradingsDynamicBearingStrategyService;
                        default:                throw new RuntimeException("strategy not handled: " + applicationCurrencyTradingsStrategy);
                    }
                });
    }

}
