package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.model.parameters.XzAsset;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ExchangeService {
   Flux<BalanceResponse> getBalance();
   Flux<String> getTradeBalance(final AssetClass assetClass);
   Flux<AddOrderOutput> addOrder(final AddOrderInput addOrderInput);
   Flux<OpenOrderOutput> getOpenOrders();
   Mono<BigDecimal> getTotalBuyPlacements(final Asset asset);
   Mono<BigDecimal> getTotalSellPlacements(final Asset asset);
   Flux<BigDecimal> getAvailableAssetForBuyPlacement(final XzAsset balanceXzAsset, final Asset openOrdersAsset);
   Flux<BigDecimal> getAvailableAssetForSellPlacement(final XzAsset balanceXzAsset, final Asset openOrdersAsset);
}
