package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.XzAsset;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.OpenOrdersMapper;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class ExchangeServiceImpl implements ExchangeService  {

    private final KrakenPrivateRepository krakenPrivateRepository;
    private final AddOrderMapper addOrderMapper;
    private final OpenOrdersMapper openOrdersMapper;

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


    public BigDecimal doSumAllOpenedOrdersForBuying() {
        getOpenOrders().filter()
        return null;
    }
*/

}
