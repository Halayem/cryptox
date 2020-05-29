package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.AddOrderInput;
import fr.enix.exchanges.model.business.AddOrderOutput;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.kraken.AssetClass;
import fr.enix.mapper.AddOrderMapper;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ExchangeServiceImpl implements ExchangeService  {

    private final KrakenPrivateRepository krakenPrivateRepository;
    private final AddOrderMapper addOrderMapper;

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
}
