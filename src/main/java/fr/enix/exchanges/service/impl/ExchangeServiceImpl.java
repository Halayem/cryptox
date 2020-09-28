package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.OpenOrdersMapper;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.BalanceOutput;
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

    @Override
    public Mono<BigDecimal> getAvailableAssetForBuyPlacement(final String applicationAssetPair) {
        return krakenPrivateRepository.getAvailableAssetForBuyPlacementByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<BigDecimal> getAvailableAssetForSellPlacement(final String applicationAssetPair) {
        return krakenPrivateRepository.getAvailableAssetForSellPlacementByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput) {
        return krakenPrivateRepository.addOrder(addOrderInput);

    }

    @Override
    public Mono<BigDecimal> getBalanceByApplicationAsset(final String applicationAsset) {
        return krakenPrivateRepository.getBalanceByApplicationAsset(applicationAsset);
    }

    @Override
    public Flux<String> getTradeBalance(final AssetClass assetClass) {
        return krakenPrivateRepository.getTradeBalance(assetClass);
    }





}
