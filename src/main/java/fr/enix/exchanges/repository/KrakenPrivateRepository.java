package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.ws.response.AddOrderResponse;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;
import fr.enix.exchanges.model.parameters.AssetClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface KrakenPrivateRepository {

    Flux<BalanceResponse> getBalance();
    Flux<String> getTradeBalance(final AssetClass assetClass);
    Flux<AddOrderResponse> addOrder(final AddOrderInput addOrderInput);
    Mono<OpenOrdersResponse> getOpenOrders();
}
