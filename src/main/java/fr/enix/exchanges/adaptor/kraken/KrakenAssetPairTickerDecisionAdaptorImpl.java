package fr.enix.exchanges.adaptor.kraken;

import fr.enix.exchanges.adaptor.ApplicationAssetPairTickerDecisionAdaptor;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import reactor.core.publisher.Mono;

public class KrakenAssetPairTickerDecisionAdaptorImpl implements ApplicationAssetPairTickerDecisionAdaptor {

    private static final int SCALE = 2;

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> adapt(final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision ) {
        return Mono.just(
                applicationAssetPairTickerTradingDecision
                .toBuilder      ()
                .price          ( applicationAssetPairTickerTradingDecision.getPrice()          == null ? null : applicationAssetPairTickerTradingDecision.getPrice().setScale         ( SCALE ) )
                .stopLossPrice  ( applicationAssetPairTickerTradingDecision.getStopLossPrice()  == null ? null : applicationAssetPairTickerTradingDecision.getStopLossPrice().setScale ( SCALE ) )
                .build          ()
        );
    }
}
