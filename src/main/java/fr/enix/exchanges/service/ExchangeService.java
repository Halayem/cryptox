package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.kraken.AssetClass;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ExchangeService {
   Flux<BalanceResponse> getBalance();
   Flux<String> getTradeBalance(final AssetClass assetClass);
   Flux<AddOrderOutput> addOrder(final AddOrderInput addOrderInput);
   Flux<List<OpenOrderOutput>> getOpenOrders();
}
