package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.common.utils.math.ApplicationMathUtils;
import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class LowGapTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final TradingBearingStrategyDecisionHelper tradingBearingStrategyDecisionHelper;
    private final ExchangeService exchangeService;
    private final AssetOrderIntervalRepository assetOrderIntervalRepository;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;
    private final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper;

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(ApplicationAssetPairTicker applicationAssetPairTicker) {
        tradingBearingStrategyDecisionHelper.updatePriceReference(applicationAssetPairTicker);

        return
            getAmountToBuy  ( applicationAssetPairTicker)
            .flatMap        ( amountToBuy -> {
                if ( amountToBuyIsLessThanTheMinimumOrder(applicationAssetPairTicker.getApplicationAssetPair(), amountToBuy) ) {
                    return applicationAssetPairTickerMapper.mapDoNothingDecision(
                            applicationAssetPairTicker,
                            String.format("the computed amount to buy: <%f>, is less than the minimum order by market", amountToBuy)
                    );
                }

                return applicationAssetPairTickerMapper.mapBuyDecision(
                        applicationAssetPairTicker,
                        amountToBuy,
                        applicationAssetPairTicker.getPrice()
                );
            });
    }

    protected Mono<BigDecimal> getAmountToBuy(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        return
            exchangeService
            .getAvailableAssetForBuyPlacementByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair())
            .flatMap(availableAssetForBuy ->
                isAvailableAssetCanBuyTheConfiguredAmount(applicationAssetPairTicker, availableAssetForBuy)
                .flatMap(isLess -> {
                    if (Boolean.TRUE.equals(isLess)) {
                        return Mono.just(ApplicationMathUtils.doDivision(availableAssetForBuy, applicationAssetPairTicker.getPrice()));
                    } else {
                        return applicationCurrencyTradingsParameterRepository.getAmountToBuyForBearingStrategyByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair());
                    }
                })
            );
    }

    private Mono<Boolean> isAvailableAssetCanBuyTheConfiguredAmount(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                    final BigDecimal availableAssetForBuy ) {
        return  applicationCurrencyTradingsParameterRepository
                .getAmountToBuyForBearingStrategyByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair())
                .map( configuredAmountToBuy ->
                    ( configuredAmountToBuy.multiply(applicationAssetPairTicker.getPrice()) ).compareTo(availableAssetForBuy) > 0
                );
    }

    private boolean amountToBuyIsLessThanTheMinimumOrder(final String applicationAssetPair,
                                                         final BigDecimal amountToBuy) {
        return amountToBuy.compareTo(assetOrderIntervalRepository.getMinimumOrderForApplicationAsset(applicationAssetPair)) < 0;
    }
}
