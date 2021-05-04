package fr.enix.exchanges.adaptor;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import reactor.core.publisher.Mono;

public interface ApplicationAssetPairTickerDecisionAdaptor {

    Mono<ApplicationAssetPairTickerTradingDecision> adapt(final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision );
}
