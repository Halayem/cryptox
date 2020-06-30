package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.model.ws.AssetPair;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ExchangeService {
   Flux<BalanceResponse> getBalance();
   Flux<String> getTradeBalance(final AssetClass assetClass);
   Flux<AddOrderOutput> addOrder(final AddOrderInput addOrderInput);
   Flux<OpenOrderOutput> getOpenOrders();
   Mono<BigDecimal> getTotalBuyPlacements(final AssetPair assetPair);
   Mono<BigDecimal> getTotalSellPlacements(final AssetPair assetPair);
}
