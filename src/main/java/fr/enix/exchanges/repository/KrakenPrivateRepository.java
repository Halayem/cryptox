package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.business.AddOrder;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.kraken.AssetClass;
import reactor.core.publisher.Flux;

public interface KrakenPrivateRepository {

    Flux<BalanceResponse> getBalance();
    Flux<String> getTradeBalance(final AssetClass assetClass);
    Flux<String> addOrder(final AddOrder addOrder);
}
