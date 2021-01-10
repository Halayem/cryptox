package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsStrategy;
import fr.enix.exchanges.service.ApplicationCurrencyTradingsParameterService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class ApplicationCurrencyTradingsParameterServiceImpl implements ApplicationCurrencyTradingsParameterService {

    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    @Override
    public Mono<BigDecimal> getGapScaleByApplicationAssetPair(final String applicationAssetPair) {
        return  applicationCurrencyTradingsParameterRepository
                .getStrategyForApplicationAssetPair ( applicationAssetPair )
                .flatMap                            ( applicationCurrencyTradingsStrategy -> getGapScale( applicationAssetPair, applicationCurrencyTradingsStrategy ) );
    }

    @Override
    public Mono<BigDecimal> getAmountToBuyByApplicationAssetPair(final String applicationAssetPair) {
        return  applicationCurrencyTradingsParameterRepository
                .getStrategyForApplicationAssetPair ( applicationAssetPair )
                .flatMap                            ( applicationCurrencyTradingsStrategy -> getAmountToBuy( applicationAssetPair, applicationCurrencyTradingsStrategy ) );

    }

    @Override
    public Mono<BigDecimal> getAmountToSellByApplicationAssetPair(final String applicationAssetPair) {
        return  applicationCurrencyTradingsParameterRepository
                .getStrategyForApplicationAssetPair ( applicationAssetPair )
                .flatMap                            ( applicationCurrencyTradingsStrategy -> getAmountToSell( applicationAssetPair, applicationCurrencyTradingsStrategy ) );

    }

    @Override
    public Mono<BigDecimal> getAmountEnhanceStepByApplicationAssetPair(final String applicationAssetPair){
        return  applicationCurrencyTradingsParameterRepository
                .getStrategyForApplicationAssetPair ( applicationAssetPair )
                .flatMap                            ( applicationCurrencyTradingsStrategy -> getAmountEnhanceStep( applicationAssetPair, applicationCurrencyTradingsStrategy ) );

    }

    private Mono<BigDecimal> getGapScale(final String applicationAssetPair,
                                         final ApplicationCurrencyTradingsStrategy applicationCurrencyTradingsStrategy) {
        switch (applicationCurrencyTradingsStrategy) {
            case STATIC_BEARING: return applicationCurrencyTradingsParameterRepository.getGapScaleByApplicationAssetPair(applicationAssetPair);
            default: throw new RuntimeException("unhandled strategy, can not get gap scale " + applicationAssetPair + " " + applicationCurrencyTradingsStrategy);
        }
    }

    private Mono<BigDecimal> getAmountToBuy(final String applicationAssetPair,
                                            final ApplicationCurrencyTradingsStrategy applicationCurrencyTradingsStrategy) {
        switch (applicationCurrencyTradingsStrategy) {
            case STATIC_BEARING: return applicationCurrencyTradingsParameterRepository.getAmountToBuyForBearingStrategyByApplicationAssetPair(applicationAssetPair);
            default: throw new RuntimeException("unhandled strategy, can not get amount to buy for " + applicationAssetPair + " " + applicationCurrencyTradingsStrategy);
        }
    }

    private Mono<BigDecimal> getAmountToSell(final String applicationAssetPair,
                                             final ApplicationCurrencyTradingsStrategy applicationCurrencyTradingsStrategy) {
        switch (applicationCurrencyTradingsStrategy) {
            case STATIC_BEARING: return applicationCurrencyTradingsParameterRepository.getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair);
            default: throw new RuntimeException("unhandled strategy, can not get amount to sell for " + applicationAssetPair + " " + applicationCurrencyTradingsStrategy);
        }
    }

    private Mono<BigDecimal> getAmountEnhanceStep(final String applicationAssetPair,
                                                  final ApplicationCurrencyTradingsStrategy applicationCurrencyTradingsStrategy) {
        switch (applicationCurrencyTradingsStrategy) {
            case STATIC_BEARING: return Mono.just(applicationCurrencyTradingsParameterRepository.getAmountEnhanceStepByApplicationAssetPair(applicationAssetPair));
            default: throw new RuntimeException("unhandled strategy, can not get amount enhance step for " + applicationAssetPair + " " + applicationCurrencyTradingsStrategy);
        }
    }

}
