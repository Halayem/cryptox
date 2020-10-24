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

    protected Mono<BigDecimal> getAmountToSell( final String applicationAssetPair ) {
        return
            exchangeService
            .getAvailableAssetForSellPlacementByApplicationAssetPair( applicationAssetPair )
            .flatMap( availableAssetForSell ->
                getComputedAmountToSell ( applicationAssetPair )
                .map( computedAmountToSell -> computedAmountToSell.compareTo( availableAssetForSell ) < 0 ? computedAmountToSell : availableAssetForSell)
            );
    }

    private Mono<BigDecimal> getComputedAmountToSell(final String applicationAssetPair) {
        return
            applicationCurrencyTradingsParameterRepository
            .getAmountToSellForBearingStrategyByApplicationAssetPair( applicationAssetPair )
            .map(configuredAmountToSell -> configuredAmountToSell.multiply( BigDecimal.valueOf( amountMultiplierService.getNewAmountMultiplierForSell( applicationAssetPair ) ) ) );
    }

}
