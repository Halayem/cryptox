package fr.enix.exchanges.strategy.bearing.impl;

import fr.enix.common.utils.math.ApplicationMathUtils;
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
public class LowGapTradingBearingStrategyDecisionImpl implements TradingBearingStrategyDecision {

    private final PriceReferenceService priceReferenceService;
    private final ExchangeService exchangeService;
    private final AssetOrderIntervalRepository assetOrderIntervalRepository;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;


    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(ApplicationAssetPairTicker applicationAssetPairTicker) {
        priceReferenceService.updatePriceReference(applicationAssetPairTicker);

        return
            getAmountToBuy(applicationAssetPairTicker)
            .flatMap(amountToBuy -> {
                if ( amountToBuy.compareTo(assetOrderIntervalRepository.getMinimumOrderForApplicationAsset(applicationAssetPairTicker.getApplicationAssetPair())) < 0 ) {
                    return
                        newTradingDecisionNoBuyWhenAmountIsLessThanMinimum(
                            applicationAssetPairTicker,
                            amountToBuy,
                            assetOrderIntervalRepository.getMinimumOrderForApplicationAsset( applicationAssetPairTicker.getApplicationAssetPair() )
                        );
                }

                return
                    Mono.just(
                        ApplicationAssetPairTickerTradingDecision
                            .builder    ()
                            .amount     (amountToBuy)
                            .price      (applicationAssetPairTicker.getPrice())
                            .operation  (
                                ApplicationAssetPairTickerTradingDecision
                                    .Operation
                                    .builder    ()
                                    .decision   (ApplicationAssetPairTickerTradingDecision.Decision.BUY)
                                    .build      ()
                            )
                            .applicationAssetPairTickerReference(applicationAssetPairTicker.toBuilder().build())
                            .build());
            });
    }

    protected Mono<BigDecimal> getAmountToBuy(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        return
            exchangeService
            .getAvailableAssetForBuyPlacementByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair())
            .flatMap(availableAssetForBuy ->
                isAvailableAssetLessThanConfiguredAmountToBuy(applicationAssetPairTicker, availableAssetForBuy)
                .flatMap(isLess ->
                    Boolean.TRUE.equals(isLess)
                    ? Mono.just(ApplicationMathUtils.doDivision( availableAssetForBuy, applicationAssetPairTicker.getPrice() ) )
                    : applicationCurrencyTradingsParameterRepository.getAmountToBuyForBearingStrategyByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair())
                )
            );
    }

    private Mono<Boolean> isAvailableAssetLessThanConfiguredAmountToBuy(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                        final BigDecimal availableAssetForBuy ) {
        return  applicationCurrencyTradingsParameterRepository
                .getAmountToBuyForBearingStrategyByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair())
                .map( configuredAmountToBuy ->
                    ( configuredAmountToBuy.multiply(applicationAssetPairTicker.getPrice()) ).compareTo(availableAssetForBuy) > 0
                );
    }

    private Mono<ApplicationAssetPairTickerTradingDecision> newTradingDecisionNoBuyWhenAmountIsLessThanMinimum(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                                                               final BigDecimal amountToBuy,
                                                                                                               final BigDecimal minimumOrder) {
        return newTradingDecisionWhenDoNothing(
                applicationAssetPairTicker,
                String.format(
                    "the computed amount to buy: <%f>, is less than the minimum order: <%f>",
                    amountToBuy,
                    minimumOrder
                ));
    }

    private Mono<ApplicationAssetPairTickerTradingDecision> newTradingDecisionWhenDoNothing(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                                            final String message) {
        return
            Mono.just(
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
