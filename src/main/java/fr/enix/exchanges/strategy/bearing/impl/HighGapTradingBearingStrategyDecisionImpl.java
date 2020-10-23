package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.strategy.bearing.AmountMultiplierService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class HighGapTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final PriceReferenceService priceReferenceService;
    private final ExchangeService exchangeService;
    private final AmountMultiplierService amountMultiplierService;
    private final AssetOrderIntervalRepository assetOrderIntervalRepository;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;
    private final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper;

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(ApplicationAssetPairTicker applicationAssetPairTicker) {
        priceReferenceService.updatePriceReference(applicationAssetPairTicker);

        return
            getAmountToSell(applicationAssetPairTicker.getApplicationAssetPair())
            .flatMap(amountToSell -> {
                if ( amountToSell.compareTo(assetOrderIntervalRepository.getMinimumOrderForApplicationAsset(applicationAssetPairTicker.getApplicationAssetPair())) < 0 ) {
                    return applicationAssetPairTickerMapper.mapDoNothingDecision(
                            applicationAssetPairTicker,
                            String.format("the computed amount to sell: <%f>, is less than the minimum order by market", amountToSell)
                    );
                } else {
                    return applicationAssetPairTickerMapper.mapSellDecision(applicationAssetPairTicker, amountToSell, applicationAssetPairTicker.getPrice() );
                }
            });
    }

    private Mono<Boolean> isAvailableAssetLessThanConfiguredAmountToSell(final String applicationAssetPair,
                                                                         final BigDecimal availableAssetForSell) {
        return applicationCurrencyTradingsParameterRepository
                .getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair)
                .map(configuredAmountToSell -> configuredAmountToSell.compareTo(availableAssetForSell) > 0 );
    }

    protected Mono<BigDecimal> getAmountToSell(final String applicationAssetPair) {
        return
            exchangeService
            .getAvailableAssetForSellPlacementByApplicationAssetPair(applicationAssetPair)
            .flatMap(availableAssetForSell ->
                isAvailableAssetLessThanConfiguredAmountToSell(applicationAssetPair, availableAssetForSell)
                .flatMap( isLess -> {
                    if (Boolean.TRUE.equals(isLess)) {
                        return Mono.just(availableAssetForSell);
                    } else {
                        return applicationCurrencyTradingsParameterRepository.getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair);
                    }
                })
            );
    }

}
