package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ApplicationCurrencyTradingsBearingStrategy;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.strategy.bearing.AmountEnhancerService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Locale;

@AllArgsConstructor
public class HighGapTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final PriceReferenceService priceReferenceService;
    private final ExchangeService exchangeService;
    private final AmountEnhancerService amountEnhancerService;
    private final ApplicationCurrencyTradingsBearingStrategy applicationCurrencyTradingsBearingStrategy;
    private final AssetOrderIntervalRepository assetOrderIntervalRepository;
    private final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper;

    @Override
    public Flux<ApplicationAssetPairTickerTradingDecision> getDecisions(ApplicationAssetPairTicker applicationAssetPairTicker) {
        priceReferenceService.updatePriceReference(applicationAssetPairTicker);

        return
            getAmountToSell(applicationAssetPairTicker.getApplicationAssetPair())
            .flatMapMany(amountToSell -> {
                if ( amountToSell.compareTo(assetOrderIntervalRepository.getMinimumOrderForApplicationAsset(applicationAssetPairTicker.getApplicationAssetPair())) < 0 ) {
                    return Flux.just(applicationAssetPairTickerMapper.mapDoNothingDecision(
                            applicationAssetPairTicker,
                            String.format( Locale.FRANCE,"the computed amount to sell: <%.6f>, is less than the minimum order by market", amountToSell)
                    ));
                } else {
                    return Flux.just(applicationAssetPairTickerMapper.mapSellDecision(applicationAssetPairTicker, amountToSell, applicationAssetPairTicker.getPrice() ));
                }
            });
    }

    protected Mono<BigDecimal> getAmountToSell( final String applicationAssetPair ) {
        return
            exchangeService
            .getAvailableAssetForSellPlacementByApplicationAssetPair( applicationAssetPair )
            .flatMap( availableAssetForSell ->
                getComputedAmountToSell ( applicationAssetPair )
                .map( computedAmountToSell -> computedAmountToSell.compareTo( availableAssetForSell ) < 0 ? computedAmountToSell : availableAssetForSell)
            );
    }

    protected Mono<BigDecimal> getComputedAmountToSell(final String applicationAssetPair) {
        return
            applicationCurrencyTradingsBearingStrategy
            .getApplicationCurrencyTradingsBearingStrategyService( applicationAssetPair )
            .flatMap( applicationCurrencyTradingsBearingStrategyService -> applicationCurrencyTradingsBearingStrategyService.getAmountToSellByApplicationAssetPair(applicationAssetPair))
            .zipWith( amountEnhancerService.getNewAmountEnhanceForSell( applicationAssetPair ) )
            .map    ( objects -> {
                final BigDecimal configuredAmountToSell     = objects.getT1();
                final BigDecimal newAmountEnhanceForSell    = objects.getT2();

                return configuredAmountToSell.add(newAmountEnhanceForSell) ;
            });
    }

}
