package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.OpenOrdersMapper;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.model.ws.AssetPair;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.service.ExchangeService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class ExchangeServiceImpl implements ExchangeService  {

    private final KrakenPrivateRepository krakenPrivateRepository;
    private final AddOrderMapper addOrderMapper;
    private final OpenOrdersMapper openOrdersMapper;

    private final BigDecimal ZERO = new BigDecimal(0);

    @Override
    public Flux<BalanceResponse> getBalance() {
        return krakenPrivateRepository.getBalance();
    }

    @Override
    public Flux<String> getTradeBalance(final AssetClass assetClass) {
        return krakenPrivateRepository.getTradeBalance(assetClass);
    }

    @Override
    public Flux<AddOrderOutput> addOrder(final AddOrderInput addOrderInput) {
        return krakenPrivateRepository.addOrder (addOrderInput)
                                      .map      (addOrderResponse -> addOrderMapper.mapAddOrderResponseToAddOrderOutput(addOrderResponse));
    }

    @Override
    public Flux<OpenOrderOutput> getOpenOrders() {
        return krakenPrivateRepository.getOpenOrders()
                                      .map          (openOrdersMapper::mapFromOpenOrdersResponseToOpenOrderOutput)
                                      .flatMapMany  (Flux::fromIterable);

    }

    /*
    public BigDecimal getAvailableFiatCurrencyForBuying() {
        getBalance().map(balanceResponse -> balanceResponse.getResult().get(XzAsset.ZEUR))
                    .map(balanceInEuro -> );

                return null;
    }
     */


    @Override
    public Mono<BigDecimal> getTotalBuyPlacements(final AssetPair assetPair) {
        return getTotalPlacements(AddOrderType.BUY, (assetPair.getFrom().toString() + assetPair.getTo().toString()));
    }

    @Override
    public Mono<BigDecimal> getTotalSellPlacements(final AssetPair assetPair) {
        return getTotalPlacements(AddOrderType.SELL, (assetPair.getFrom().toString() + assetPair.getTo().toString()));
    }

    private Mono<BigDecimal> getTotalPlacements(final AddOrderType addOrderType, final String assetPair) {
        return getOpenOrders()
                .filter(openOrderOutput ->
                    (addOrderType.equals(openOrderOutput.getOrderType()) )
                    &&
                    assetPair.equals(openOrderOutput.getAssetPair())
                )
                .map(openOrderOutput ->
                        openOrderOutput.getPrice().multiply(openOrderOutput.getVolume())
                )
                .reduce(ZERO, (blockedFiatCurrencyForBuying1, blockedFiatCurrencyForBuying2) ->
                        blockedFiatCurrencyForBuying1.add(blockedFiatCurrencyForBuying2)
                );
    }

}
