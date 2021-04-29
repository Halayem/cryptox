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

    public Mono<ApplicationAssetPairTickerTradingDecision> mapBuyStopLossDecision(final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                                  final BigDecimal amount,
                                                                                  final BigDecimal price,
                                                                                  final BigDecimal stopLossPrice) {
        return mapDecision(
                ApplicationAssetPairTickerTradingDecision.Decision.BUY_STOP_LOSS,
                applicationAssetPairTickerReference,
                amount,
                price,
                stopLossPrice
        );
    }

    public Mono<ApplicationAssetPairTickerTradingDecision> mapSellDecision(final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                           final BigDecimal amount,
                                                                           final BigDecimal price) {
        return mapDecision(
                ApplicationAssetPairTickerTradingDecision.Decision.SELL,
                applicationAssetPairTickerReference,
                amount,
                price,
                null
        );
    }

    private Mono<ApplicationAssetPairTickerTradingDecision> mapDecision(final ApplicationAssetPairTickerTradingDecision.Decision decision,
                                                                        final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                        final BigDecimal amount,
                                                                        final BigDecimal price,
                                                                        final BigDecimal stopLossPrice) {
        return Mono.just(
                ApplicationAssetPairTickerTradingDecision
                .builder        ()
                .amount         ( amount )
                .price          ( price  )
                .stopLossPrice  ( stopLossPrice )
                .operation      (
                    ApplicationAssetPairTickerTradingDecision
                    .Operation
                    .builder    ()
                    .decision   (decision)
                    .build      ()
                )
                .applicationAssetPairTickerReference(applicationAssetPairTickerReference.toBuilder().build())
                .build()
        );
    }
}
