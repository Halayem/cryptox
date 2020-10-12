package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface KrakenPrivateRepository {

    Mono<BigDecimal> getAvailableAssetForSellPlacementByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getAvailableAssetForBuyPlacementByApplicationAssetPair(final String applicationAssetPair);
    Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput);
}
