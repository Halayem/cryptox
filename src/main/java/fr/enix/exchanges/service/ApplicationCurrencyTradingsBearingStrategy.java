package fr.enix.exchanges.service;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsStrategy;
import fr.enix.exchanges.service.impl.ApplicationCurrencyTradingsDynamicBearingStrategyServiceImpl;
import fr.enix.exchanges.service.impl.ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApplicationCurrencyTradingsBearingStrategy {

    private final ApplicationCurrencyTradingsDynamicBearingStrategyServiceImpl  applicationCurrencyTradingsDynamicBearingStrategyService;
    private final ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl   applicationCurrencyTradingsStaticBearingStrategyService;

    public ApplicationCurrencyTradingsBearingStrategyService getApplicationCurrencyTradingsBearingStrategyService(final ApplicationCurrencyTradingsStrategy applicationCurrencyTradingsStrategy) {
        switch (applicationCurrencyTradingsStrategy) {
            case STATIC_BEARING:    return applicationCurrencyTradingsStaticBearingStrategyService;
            case DYNAMIC_BEARING:   return applicationCurrencyTradingsDynamicBearingStrategyService;
            default:                throw new RuntimeException("strategy not handled: " + applicationCurrencyTradingsStrategy);
        }

    }

}
