package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.common.utils.math.ApplicationMathUtils;
import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.strategy.bearing.AmountEnhancerService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Locale;

@AllArgsConstructor
public class LowGapTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final PriceReferenceService priceReferenceService;
    private final ExchangeService exchangeService;
    private final AmountEnhancerService amountEnhancerService;
    private final AssetOrderIntervalRepository assetOrderIntervalRepository;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;
    private final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper;

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(ApplicationAssetPairTicker applicationAssetPairTicker) {
        priceReferenceService.updatePriceReference(applicationAssetPairTicker);

        return
            getAmountToBuy  ( applicationAssetPairTicker)
            .flatMap        ( amountToBuy -> {
                if ( amountToBuyIsLessThanTheMinimumOrder(applicationAssetPairTicker.getApplicationAssetPair(), amountToBuy) ) {
                    return applicationAssetPairTickerMapper.mapDoNothingDecision(
                            applicationAssetPairTicker,
                            String.format( Locale.FRANCE, "the computed amount to buy: <%.6f>, is less than the minimum order by market", amountToBuy)
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
            Mono.zip(
                getComputedAmountToBuy(applicationAssetPairTicker.getApplicationAssetPair()),
                exchangeService.getAvailableAssetForBuyPlacementByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair())
            )
            .flatMap( objects -> {
                final BigDecimal computedAmountToBuy    = objects.getT1();
                final BigDecimal availableAssetForBuy   = objects.getT2();

                return isAvailableAssetCanBuyTheComputedAmount(applicationAssetPairTicker, availableAssetForBuy, computedAmountToBuy )
                        ? Mono.just( computedAmountToBuy )
                        : Mono.just(ApplicationMathUtils.doDivision(availableAssetForBuy, applicationAssetPairTicker.getPrice()));
            });
    }

    private Mono<BigDecimal> getComputedAmountToBuy(final String applicationAssetPair) {
        return
            applicationCurrencyTradingsParameterRepository
            .getAmountToBuyForBearingStrategyByApplicationAssetPair( applicationAssetPair )
            .map(configuredAmountToBuy -> configuredAmountToBuy.add( amountEnhancerService.getNewAmountEnhanceForBuy( applicationAssetPair ) ) );
    }

    private boolean isAvailableAssetCanBuyTheComputedAmount(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                  final BigDecimal availableAssetForBuy,
                                                                  final BigDecimal computedAmountToBuy) {
        return computedAmountToBuy.multiply(applicationAssetPairTicker.getPrice()).compareTo(availableAssetForBuy) < 0;
    }

    private boolean amountToBuyIsLessThanTheMinimumOrder(final String applicationAssetPair,
                                                         final BigDecimal amountToBuy) {
        return amountToBuy.compareTo(assetOrderIntervalRepository.getMinimumOrderForApplicationAsset(applicationAssetPair)) < 0;
    }
}
