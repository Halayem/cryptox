package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.parameters.ApplicationCurrencyTradingsParameter;
import fr.enix.exchanges.repository.ApplicationBearingStrategyParameterRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class ApplicationCurrencyBearingStrategyTradingsParameterRepositoryImpl implements ApplicationBearingStrategyParameterRepository {

    private final ApplicationCurrencyTradingsParameter applicationCurrencyTradingsParameter;

    @Override
    public Mono<BigDecimal> getGapByApplicationAssetPair(String applicationAssetPair) {
        return Mono.just(
                applicationCurrencyTradingsParameter
                .getTradings()
                .get(applicationAssetPair)
                .getBearingStrategy()
                .getGap()
        );
    }

    @Override
    public Mono<BigDecimal> getAmountToSellByApplicationAssetPair(String applicationAssetPair) {
        return Mono.just(
                applicationCurrencyTradingsParameter
                .getTradings()
                .get(applicationAssetPair)
                .getBearingStrategy()
                .getAmountToSell()
        );
    }

    @Override
    public Mono<BigDecimal> getAmountToBuyByApplicationAssetPair(String applicationAssetPair) {
        return Mono.just(
                applicationCurrencyTradingsParameter
                .getTradings()
                .get(applicationAssetPair)
                .getBearingStrategy()
                .getAmountToBuy()
        );
    }

    @Override
    public Mono<BigDecimal> getAmountEnhanceByApplicationAssetPair(String applicationAssetPair) {
        return null;
    }
}
