package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.BalanceOutput;
import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.model.parameters.XzAsset;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ExchangeService {
   Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput);
   Mono<BigDecimal> getAvailableAssetForBuyPlacementByApplicationAssetPair(final String applicationAssetPair);
   Mono<BigDecimal> getAvailableAssetForSellPlacementByApplicationAssetPair(final String applicationAssetPair);
}
