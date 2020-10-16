package fr.enix.exchanges.mapper;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
public class ApplicationAssetPairTickerMapper {

    public Mono<ApplicationAssetPairTickerTradingDecision> mapDoNothingDecision(final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                                final String message) {

        return Mono.just(
                ApplicationAssetPairTickerTradingDecision
                .builder    ()
                .operation  (
                    ApplicationAssetPairTickerTradingDecision
                        .Operation
                        .builder    ()
                        .decision   (ApplicationAssetPairTickerTradingDecision.Decision.DO_NOTHING)
                        .message    (message)
                        .build      ()
                )
                .applicationAssetPairTickerReference(applicationAssetPairTickerReference.toBuilder().build())
                .build()
        );
    }

    public Mono<ApplicationAssetPairTickerTradingDecision> mapBuyDecision(final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                          final BigDecimal amount,
                                                                          final BigDecimal price) {
        return Mono.just(
                ApplicationAssetPairTickerTradingDecision
                .builder    ()
                .amount     ( amount )
                .price      ( price  )
                .operation  (
                    ApplicationAssetPairTickerTradingDecision
                    .Operation
                    .builder    ()
                    .decision   (ApplicationAssetPairTickerTradingDecision.Decision.BUY)
                    .build      ()
                )
                .applicationAssetPairTickerReference(applicationAssetPairTickerReference.toBuilder().build())
                .build()
        );
    }
}
