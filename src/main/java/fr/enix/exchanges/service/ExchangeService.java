package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ExchangeService {
   Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput);
   Mono<BigDecimal> getAvailableAssetForBuyPlacementByApplicationAssetPair(final String applicationAssetPair);
   Mono<BigDecimal> getAvailableAssetForSellPlacementByApplicationAssetPair(final String applicationAssetPair);
}
