package fr.enix.exchanges.strategy.bearing;

import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.service.PriceReferenceService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

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
                priceReferenceService.getPriceReferenceForApplicationAssetPair(applicationAssetPair).map(priceReference -> priceReference.getPrice()),
                applicationCurrencyTradingsParameterRepository.getGapScaleByApplicationAssetPair(applicationAssetPair)
            )
            .flatMap(objects -> {
                final BigDecimal priceReference = objects.getT1();
                final BigDecimal gap            = objects.getT2();
                final GapStatus gapStatus       = getGapStatus(currentApplicationAssetPairPrice, priceReference, gap);

                switch( gapStatus ) {
                    case HIGH_REACHED:  return Mono.just(highGapTradingBearingStrategyDecisionImpl  );
                    case LOW_REACHED:   return Mono.just(lowGapTradingBearingStrategyDecisionImpl   );
                    case NOT_REACHED:   return Mono.just(doNothingTradingBearingStrategyDecisionImpl);
                    default:            throw new RuntimeException("unhandled gap status: " + gapStatus.toString());
                }
            })
            .switchIfEmpty(Mono.just(errorTradingBearingStrategyDecisionImpl));
    }


    private GapStatus getGapStatus(final BigDecimal lastPrice,
                                   final BigDecimal priceReference,
                                   final BigDecimal gap) {

        if ( lastPrice.compareTo(priceReference.add(gap))       >= 0 ) { return GapStatus.HIGH_REACHED;    }
        if ( lastPrice.compareTo(priceReference.subtract(gap))  <= 0 ) { return GapStatus.LOW_REACHED;     }
        return GapStatus.NOT_REACHED;
    }

    private enum GapStatus {
        HIGH_REACHED, LOW_REACHED, NOT_REACHED
    }
}
