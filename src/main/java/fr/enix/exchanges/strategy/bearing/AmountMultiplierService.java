package fr.enix.exchanges.strategy.bearing;

import reactor.core.publisher.Mono;

public interface AmountMultiplierService {

    Mono<Integer> getAmountMultiplierForSell(final String applicationAssetPair);
    Mono<Integer> getAmountMultiplierForBuy(final String applicationAssetPair);
}
