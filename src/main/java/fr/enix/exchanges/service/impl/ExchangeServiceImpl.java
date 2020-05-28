package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.AddOrder;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.kraken.AssetClass;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ExchangeServiceImpl implements ExchangeService  {

    private final KrakenPrivateRepository krakenPrivateRepository;

    @Override
    public Flux<BalanceResponse> getBalance() {
        return krakenPrivateRepository.getBalance();
    }

    @Override
    public Flux<String> getTradeBalance(final AssetClass assetClass) {
        return krakenPrivateRepository.getTradeBalance(assetClass);
    }

    @Override
    public Flux<String> addOrder(final AddOrder addOrder) {
        return krakenPrivateRepository.addOrder(addOrder);
    }
}
