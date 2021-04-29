package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.service.ApplicationCurrencyTradingsBearingStrategyService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl implements ApplicationCurrencyTradingsBearingStrategyService {

    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    @Override
    public Mono<BigDecimal> getGapScaleByApplicationAssetPair(final String applicationAssetPair) {
        return applicationCurrencyTradingsParameterRepository.getGapScaleByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<BigDecimal> getAmountToBuyByApplicationAssetPair(final String applicationAssetPair) {
        return applicationCurrencyTradingsParameterRepository.getAmountToBuyForBearingStrategyByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<BigDecimal> getAmountToSellByApplicationAssetPair(final String applicationAssetPair) {
        return applicationCurrencyTradingsParameterRepository.getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<BigDecimal> getBuyStopLossByApplicationAssetPair(final String applicationAssetPair) {
        return applicationCurrencyTradingsParameterRepository.getBuyStopLossByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<BigDecimal> getAmountEnhanceStepByApplicationAssetPair(final String applicationAssetPair) {
        return Mono.just(applicationCurrencyTradingsParameterRepository.getAmountEnhanceStepByApplicationAssetPair(applicationAssetPair));
    }
}
