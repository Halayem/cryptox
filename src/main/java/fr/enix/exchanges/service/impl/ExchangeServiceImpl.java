package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.repository.ExchangeRepository;
import fr.enix.exchanges.service.ExchangeService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class ExchangeServiceImpl implements ExchangeService  {

    private final ExchangeRepository exchangeRepository;

    @Override
    public Mono<BigDecimal> getAvailableAssetForBuyPlacementByApplicationAssetPair(final String applicationAssetPair) {
        return exchangeRepository.getAvailableAssetForBuyPlacementByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<BigDecimal> getAvailableAssetForSellPlacementByApplicationAssetPair(final String applicationAssetPair) {
        return exchangeRepository.getAvailableAssetForSellPlacementByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput) {
        return exchangeRepository.addOrder(addOrderInput);

    }
}
