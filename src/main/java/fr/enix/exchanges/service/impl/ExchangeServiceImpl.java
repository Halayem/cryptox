package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.OpenOrdersMapper;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.model.parameters.XzAsset;
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

    private final BigDecimal zero = new BigDecimal(0);

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
                                      .map      (addOrderMapper::mapAddOrderResponseToAddOrderOutput);
    }

    @Override
    public Flux<OpenOrderOutput> getOpenOrders() {
        return krakenPrivateRepository.getOpenOrders()
                                      .map          (openOrdersMapper::mapFromOpenOrdersResponseToOpenOrderOutput)
                                      .flatMapMany  (Flux::fromIterable);

    }

    @Override
    public Flux<BigDecimal> getAvailableAssetForBuyPlacement(final XzAsset balanceXzAsset, final Asset openOrdersAsset) {

        return Flux.zip(
            getBalance(), getTotalBuyPlacements(openOrdersAsset),
            (balanceResponse, totalBuyPlacements) -> balanceResponse.getResult  ()
                                                                    .get        (balanceXzAsset)
                                                                    .subtract   (totalBuyPlacements)
        );
    }

    @Override
    public Flux<BigDecimal> getAvailableAssetForSellPlacement(final XzAsset balanceXzAsset, final Asset openOrdersAsset) {
        return Flux.zip(
                getBalance(), getTotalSellPlacements(openOrdersAsset),
                (balanceResponse, totalSellPlacements) -> balanceResponse.getResult  ()
                                                                        .get        (balanceXzAsset)
                                                                        .subtract   (totalSellPlacements)
        );
    }

    @Override
    public Mono<BigDecimal> getTotalBuyPlacements(final Asset asset) {
        return getTotalPlacements(AddOrderType.BUY, asset.toString());
    }

    @Override
    public Mono<BigDecimal> getTotalSellPlacements(final Asset asset) {
        return getTotalPlacements(AddOrderType.SELL, asset.toString());
    }

    private Mono<BigDecimal> getTotalPlacements(final AddOrderType addOrderType, final String asset) {
        return getOpenOrders()
                .filter(openOrderOutput ->
                    (addOrderType.equals(openOrderOutput.getOrderType()) )
                    &&
                    (openOrderOutput.getAssetPair().contains(asset))
                )
                .map(openOrderOutput ->
                        AddOrderType.SELL.equals(addOrderType)
                        ? openOrderOutput.getVolume()
                        : openOrderOutput.getPrice().multiply(openOrderOutput.getVolume())
                )
                .reduce(zero, (blockedFiatCurrencyForBuying1, blockedFiatCurrencyForBuying2) ->
                        blockedFiatCurrencyForBuying1.add(blockedFiatCurrencyForBuying2)
                );
    }

}
