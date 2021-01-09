package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsStrategy;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class ApplicationCurrencyTradingsParameterServiceImpl {

    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    public Mono<BigDecimal> getGapScaleByApplicationAssetPair(final String applicationAssetPair) {
        return  applicationCurrencyTradingsParameterRepository
                .getStrategyForApplicationAssetPair ( applicationAssetPair )
                .flatMap                            ( applicationCurrencyTradingsStrategy -> getGapScale( applicationAssetPair, applicationCurrencyTradingsStrategy ) );
    }

    public Mono<BigDecimal> getAmountToSellByApplicationAssetPair(final String applicationAssetPair) {


        private Mono<BigDecimal> getGapScale(final String applicationAssetPair,
                                         final ApplicationCurrencyTradingsStrategy applicationCurrencyTradingsStrategy) {
        switch (applicationCurrencyTradingsStrategy) {
            case STATIC_BEARING: return applicationCurrencyTradingsParameterRepository.getGapScaleByApplicationAssetPair(applicationAssetPair);
            default: throw new RuntimeException("unhandled gap scale for " + applicationAssetPair + " " + applicationCurrencyTradingsStrategy);
        }
    }


}
