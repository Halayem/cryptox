package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.ws.response.CancelOrderResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ExchangeRepository {

    Mono<BigDecimal> getAvailableAssetForSellPlacementByApplicationAssetPair(final String applicationAssetPair);
    Mono<BigDecimal> getAvailableAssetForBuyPlacementByApplicationAssetPair(final String applicationAssetPair);
    Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput);
    Mono<CancelOrderResponse> cancelOrder(final String transactionId);
}
