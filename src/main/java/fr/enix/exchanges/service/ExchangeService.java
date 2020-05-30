package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.AddOrderInput;
import fr.enix.exchanges.model.business.AddOrderOutput;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.kraken.AssetClass;
import reactor.core.publisher.Flux;

public interface ExchangeService {
   Flux<BalanceResponse> getBalance();
   Flux<String> getTradeBalance(final AssetClass assetClass);
   Flux<AddOrderOutput> addOrder(final AddOrderInput addOrderInput);
}
