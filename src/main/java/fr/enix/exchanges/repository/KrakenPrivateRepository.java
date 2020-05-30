package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.business.AddOrderInput;
import fr.enix.exchanges.model.ws.response.AddOrderResponse;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.kraken.AssetClass;
import reactor.core.publisher.Flux;

public interface KrakenPrivateRepository {

    Flux<BalanceResponse> getBalance();
    Flux<String> getTradeBalance(final AssetClass assetClass);
    Flux<AddOrderResponse> addOrder(final AddOrderInput addOrderInput);
}
