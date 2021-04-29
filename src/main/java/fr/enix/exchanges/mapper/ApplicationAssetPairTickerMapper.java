package fr.enix.exchanges.mapper;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
public class ApplicationAssetPairTickerMapper {

    public ApplicationAssetPairTickerTradingDecision mapDoNothingDecision(final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                                final String message) {

        return
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
                .build();
    }

    public ApplicationAssetPairTickerTradingDecision mapBuyDecision(final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                          final BigDecimal amount,
                                                                          final BigDecimal price) {
        return mapDecision(
                ApplicationAssetPairTickerTradingDecision.Decision.BUY,
                applicationAssetPairTickerReference,
                amount,
                price
        );
    }

    public ApplicationAssetPairTickerTradingDecision mapSellDecision(final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                     final BigDecimal amount,
                                                                     final BigDecimal price) {
        return mapDecision(
                ApplicationAssetPairTickerTradingDecision.Decision.SELL,
                applicationAssetPairTickerReference,
                amount,
                price
        );
    }

    private ApplicationAssetPairTickerTradingDecision mapDecision(final ApplicationAssetPairTickerTradingDecision.Decision decision,
                                                                        final ApplicationAssetPairTicker applicationAssetPairTickerReference,
                                                                        final BigDecimal amount,
                                                                        final BigDecimal price) {
        return
                ApplicationAssetPairTickerTradingDecision
                .builder    ()
                .amount     ( amount )
                .price      ( price  )
                .operation  (
                    ApplicationAssetPairTickerTradingDecision
                    .Operation
                    .builder    ()
                    .decision   (decision)
                    .build      ()
                )
                .applicationAssetPairTickerReference(applicationAssetPairTickerReference.toBuilder().build())
                .build();
    }
}
