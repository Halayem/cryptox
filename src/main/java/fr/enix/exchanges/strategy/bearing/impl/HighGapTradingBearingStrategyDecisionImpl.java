package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class HighGapTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final PriceReferenceService priceReferenceService;
    private final ExchangeService exchangeService;
    private final AssetOrderIntervalRepository assetOrderIntervalRepository;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;


    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(ApplicationAssetPairTicker applicationAssetPairTicker) {
        priceReferenceService.updatePriceReference(applicationAssetPairTicker);

        return
            getAmountToSell(applicationAssetPairTicker.getApplicationAssetPair())
            .flatMap(amountToSell -> {
                if ( amountToSell.compareTo(assetOrderIntervalRepository.getMinimumOrderForApplicationAsset(applicationAssetPairTicker.getApplicationAssetPair())) < 0 ) {
                    return
                        newTradingDecisionNoSellWhenAmountIsLessThanMinimum(
                                applicationAssetPairTicker,
                                amountToSell,
                                assetOrderIntervalRepository.getMinimumOrderForApplicationAsset( applicationAssetPairTicker.getApplicationAssetPair() )
                        );
                }

                return
                    Mono.just(
                        ApplicationAssetPairTickerTradingDecision
                            .builder    ()
                            .amount     (amountToSell)
                            .price      (applicationAssetPairTicker.getPrice())
                            .operation  (
                                ApplicationAssetPairTickerTradingDecision
                                    .Operation
                                    .builder    ()
                                    .decision   (ApplicationAssetPairTickerTradingDecision.Decision.SELL)
                                    .build      ()
                            )
                            .applicationAssetPairTickerReference(applicationAssetPairTicker.toBuilder().build())
                            .build()
                    );
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
                                        .flatMap(isLess ->
                                                Boolean.TRUE.equals(isLess)
                                                        ? Mono.just(availableAssetForSell)
                                                        : applicationCurrencyTradingsParameterRepository.getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair)
                                        )
                        );
    }

    private Mono<ApplicationAssetPairTickerTradingDecision> newTradingDecisionNoSellWhenAmountIsLessThanMinimum(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                                                                final BigDecimal amountToSell,
                                                                                                                final BigDecimal minimumOrder) {
        return newTradingDecisionWhenDoNothing(
                applicationAssetPairTicker,
                String.format(
                        "the computed amount to sell: <%f>, is less than the minimum order: <%f>",
                        amountToSell,
                        minimumOrder
                ));
    }

    private Mono<ApplicationAssetPairTickerTradingDecision> newTradingDecisionWhenDoNothing(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                                            final String message) {
        return Mono.just(
                ApplicationAssetPairTickerTradingDecision
                        .builder    ()
                        .operation  (
                                ApplicationAssetPairTickerTradingDecision
                                        .Operation
                                        .builder  ()
                                        .decision (ApplicationAssetPairTickerTradingDecision.Decision.DO_NOTHING)
                                        .message  (message)
                                        .build    ()
                        )
                        .applicationAssetPairTickerReference( applicationAssetPairTicker.toBuilder().build() )
                        .build()
        );
    }

}
