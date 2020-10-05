package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.parameters.ApplicationCurrencyTradingsParameter;
import fr.enix.exchanges.repository.ApplicationThresholdStrategyParameterRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class ApplicationThresholdStrategyParameterRepositoryImpl implements ApplicationThresholdStrategyParameterRepository {

    private final ApplicationCurrencyTradingsParameter applicationCurrencyTradingsParameter;

    @Override
    public Mono<BigDecimal> getTriggerPriceToBuyByApplicationAssetPair(String applicationAssetPair) {
         return Mono.just(
                applicationCurrencyTradingsParameter
                        .getTradings()
                        .get(applicationAssetPair)
                        .getThresholdStrategy()
                        .getTriggerPriceToBuy()
        );
    }

    @Override
    public Mono<BigDecimal> getAmountToBuyByApplicationAssetPair(String applicationAssetPair) {
        return Mono.just(
                applicationCurrencyTradingsParameter
                        .getTradings()
                        .get(applicationAssetPair)
                        .getThresholdStrategy()
                        .getAmountToBuy()
        );
    }

    @Override
    public Mono<BigDecimal> getTriggerPriceToSellByApplicationAssetPair(String applicationAssetPair) {
        return Mono.just(
                applicationCurrencyTradingsParameter
                        .getTradings()
                        .get(applicationAssetPair)
                        .getThresholdStrategy()
                        .getTriggerPriceToSell()
        );
    }

    @Override
    public Mono<BigDecimal> getAmountToSellByApplicationAssetPair(String applicationAssetPair) {
        return Mono.just(
                applicationCurrencyTradingsParameter
                        .getTradings()
                        .get(applicationAssetPair)
                        .getThresholdStrategy()
                        .getAmountToSell()
        );
    }
}
