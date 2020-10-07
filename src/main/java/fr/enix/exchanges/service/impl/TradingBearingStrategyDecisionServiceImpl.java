package fr.enix.exchanges.service.impl;

import fr.enix.common.utils.math.ApplicationMathUtils;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.service.TradingDecisionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision.Decision;

@AllArgsConstructor
@Slf4j
public class TradingBearingStrategyDecisionServiceImpl implements TradingDecisionService {


    private final PriceReferenceService                             priceReferenceService;
    private final ExchangeService                                   exchangeService;
    private final ApplicationCurrencyTradingsParameterRepository    applicationCurrencyTradingsParameterRepository;
    private final AssetOrderIntervalRepository                      assetOrderIntervalRepository;

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(final ApplicationAssetPairTicker applicationAssetPairTicker) {

        final String applicationAssetPair                   = applicationAssetPairTicker.getApplicationAssetPair();
        final BigDecimal currentApplicationAssetPairPrice   = applicationAssetPairTicker.getPrice();

        return
            Mono.zip(
                priceReferenceService.getPriceReferenceForApplicationAssetPair(applicationAssetPair).map(priceReference -> priceReference.getPrice()),
                applicationCurrencyTradingsParameterRepository.getGapScaleByApplicationAssetPair(applicationAssetPair)
            )
            .flatMap(objects -> {
                final BigDecimal priceReference = objects.getT1();
                final BigDecimal gap            = objects.getT2();

                if ( isHighGapReached( currentApplicationAssetPairPrice, priceReference, gap ) ) {
                    return manageDecisionWhenHighGapReached( applicationAssetPairTicker );
                }

                if ( isLowGapReached( currentApplicationAssetPairPrice, priceReference, gap ) ) {
                    return manageDecisionWhenLowGapReached( applicationAssetPairTicker );
                }

                return newTradingDecisionWhenDoNothing(
                        applicationAssetPairTicker,
                          String.format(
                              "gap: <%f> is not reached yet, price reference: <%f>, current price: <%f>",
                              gap,
                              priceReference,
                              currentApplicationAssetPairPrice
                          )
                );
            })
            .switchIfEmpty(
                newTradingDecisionError(
                    applicationAssetPairTicker,
                    String.format(
                        "price reference was not set for this application asset pair: <%s>",
                        applicationAssetPair
                    )
                )
            );
    }

    protected Mono<ApplicationAssetPairTickerTradingDecision> manageDecisionWhenHighGapReached(final ApplicationAssetPairTicker applicationAssetPairTicker) {
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
                            .decision   (Decision.SELL)
                            .build      ()
                        )
                        .applicationAssetPairTickerReference(applicationAssetPairTicker.toBuilder().build())
                        .build()
                    );
            });
    }

    protected Mono<ApplicationAssetPairTickerTradingDecision> manageDecisionWhenLowGapReached(final ApplicationAssetPairTicker applicationAssetPairTicker) {
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
                            .decision   (Decision.BUY)
                            .build      ()
                        )
                        .applicationAssetPairTickerReference(applicationAssetPairTicker.toBuilder().build())
                        .build());
            });
    }

    protected Mono<BigDecimal> getAmountToSell(final String applicationAssetPair) {
        return
            exchangeService
            .getAvailableAssetForSellPlacementByApplicationAssetPair(applicationAssetPair)
            .flatMap(availableAssetForSell ->
                        isAvailableAssetLessThanConfiguredAmountToSell(applicationAssetPair, availableAssetForSell)
                        .flatMap(isLess ->
                            isLess
                            ? Mono.just(availableAssetForSell)
                            : applicationCurrencyTradingsParameterRepository.getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair)
                        )
            );
    }

    protected Mono<BigDecimal> getAmountToBuy(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        return
            exchangeService
            .getAvailableAssetForBuyPlacementByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair())
            .flatMap(availableAssetForBuy ->
                        isAvailableAssetLessThanConfiguredAmountToBuy(applicationAssetPairTicker, availableAssetForBuy)
                        .flatMap(isLess ->
                            isLess
                            ? Mono.just(ApplicationMathUtils.doDivision( availableAssetForBuy, applicationAssetPairTicker.getPrice() ) )
                            : applicationCurrencyTradingsParameterRepository.getAmountToBuyForBearingStrategyByApplicationAssetPair(applicationAssetPairTicker.getApplicationAssetPair())
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
        return Mono.just(
                ApplicationAssetPairTickerTradingDecision
                .builder    ()
                .operation  (
                    ApplicationAssetPairTickerTradingDecision
                    .Operation
                    .builder  ()
                    .decision (Decision.DO_NOTHING)
                    .message  (message)
                    .build    ()
                )
                .applicationAssetPairTickerReference( applicationAssetPairTicker.toBuilder().build() )
                .build()
        );
    }

    private Mono<ApplicationAssetPairTickerTradingDecision> newTradingDecisionError(final ApplicationAssetPairTicker applicationAssetPairTicker,
                                                                                    final String message) {
        return Mono.just(
                    ApplicationAssetPairTickerTradingDecision
                    .builder    ()
                    .operation  (
                        ApplicationAssetPairTickerTradingDecision
                            .Operation
                            .builder  ()
                            .decision (Decision.ERROR)
                            .message  (message)
                            .build    ()
                    )
                    .applicationAssetPairTickerReference(applicationAssetPairTicker.toBuilder().build())
                    .build()
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

    private Mono<Boolean> isAvailableAssetLessThanConfiguredAmountToSell(final String applicationAssetPair,
                                                                         final BigDecimal availableAssetForSell) {
        return applicationCurrencyTradingsParameterRepository
                .getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair)
                .map(configuredAmountToSell -> configuredAmountToSell.compareTo(availableAssetForSell) > 0 );
    }


    private boolean isHighGapReached(final BigDecimal lastPrice,
                                     final BigDecimal priceReference,
                                     final BigDecimal gap) {
        return lastPrice.compareTo(priceReference.add(gap)) >= 0;
    }

    private boolean isLowGapReached(final BigDecimal lastPrice,
                                    final BigDecimal priceReference,
                                    final BigDecimal gap) {
        return lastPrice.compareTo(priceReference.subtract(gap)) <= 0;
    }
}
