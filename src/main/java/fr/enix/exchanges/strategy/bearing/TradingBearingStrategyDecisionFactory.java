package fr.enix.exchanges.strategy.bearing;

import fr.enix.common.exception.FactoryException;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.model.repository.PriceReference;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.service.PriceReferenceService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

// @TODO rename to TradingBearingStrategyDecisionStrategy
@AllArgsConstructor
public class TradingBearingStrategyDecisionFactory {

    private final PriceReferenceService priceReferenceService;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    private TradingBearingStrategyDecision lowGapTradingBearingStrategyDecisionImpl;
    private TradingBearingStrategyDecision highGapTradingBearingStrategyDecisionImpl;
    private TradingBearingStrategyDecision doNothingTradingBearingStrategyDecisionImpl;
    private TradingBearingStrategyDecision errorTradingBearingStrategyDecisionImpl;

    public Mono<TradingBearingStrategyDecision> getTradingBearingStrategy(final ApplicationAssetPairTicker applicationAssetPairTicker) {
        final String applicationAssetPair                   = applicationAssetPairTicker.getApplicationAssetPair();
        final BigDecimal currentApplicationAssetPairPrice   = applicationAssetPairTicker.getPrice();

        return
            Mono.zip(
                priceReferenceService.getPriceReferenceForApplicationAssetPair(applicationAssetPair).map(PriceReference::getPrice),
                applicationCurrencyTradingsParameterRepository.getGapScaleByApplicationAssetPair(applicationAssetPair)
            )
            .flatMap        ( objects ->  Mono.just( getTradingBearingStrategyInstance(currentApplicationAssetPairPrice, objects.getT1(), objects.getT2())) )
            .switchIfEmpty  ( Mono.just(errorTradingBearingStrategyDecisionImpl));
    }

    protected TradingBearingStrategyDecision getTradingBearingStrategyInstance(final BigDecimal currentApplicationAssetPairPrice,
                                                                               final BigDecimal priceReference,
                                                                               final BigDecimal gap) {

        final GapStatus gapStatus = getGapStatus( currentApplicationAssetPairPrice, priceReference, gap );
        switch( gapStatus ) {
            case HIGH_REACHED:  return highGapTradingBearingStrategyDecisionImpl    ;
            case LOW_REACHED:   return lowGapTradingBearingStrategyDecisionImpl     ;
            case NOT_REACHED:   return doNothingTradingBearingStrategyDecisionImpl  ;
            default:            throw new FactoryException("unhandled gap status: " + gapStatus.toString());
        }
    }

    protected GapStatus getGapStatus(final BigDecimal lastPrice,
                                     final BigDecimal priceReference,
                                     final BigDecimal gap) {

        if ( lastPrice.compareTo(priceReference.add(gap))       >= 0 ) { return GapStatus.HIGH_REACHED;    }
        if ( lastPrice.compareTo(priceReference.subtract(gap))  <= 0 ) { return GapStatus.LOW_REACHED;     }
        return GapStatus.NOT_REACHED;
    }

    protected enum GapStatus {
        HIGH_REACHED, LOW_REACHED, NOT_REACHED
    }

}
