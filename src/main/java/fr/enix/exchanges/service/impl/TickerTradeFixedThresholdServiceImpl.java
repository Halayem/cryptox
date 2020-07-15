package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.constant.AssetMinimumOrder;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.TickerOutput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.parameters.OrderType;
import fr.enix.exchanges.model.parameters.XzAsset;
import fr.enix.exchanges.model.websocket.AssetPair;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.service.TransactionDecisionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
public class TickerTradeFixedThresholdServiceImpl implements TickerService {

    private final ExchangeService            exchangeService;
    private final MarketOfferService         marketOfferService;
    private final TransactionDecisionService transactionDecisionService;

    private final BigDecimal ZERO = new BigDecimal(0);

    @Override
    public void marketOfferUpdateHandler(TickerOutput tickerOutput) {
        marketOfferService.saveNewMarketPrice(tickerOutput.getAssetPair(), tickerOutput.getAsk().getPrice())
                .flatMap (marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory))
                .map     (decision -> {
                    switch (decision) {
                        case SELL: return placeSellOrder  (tickerOutput)
                                          .subscribe      (addOrderOutput ->
                                            log.info("sell order placed, response: {}", addOrderOutput)
                                          );
                        case BUY:  return placeBuyOrder   (tickerOutput)
                                          .subscribe      (addOrderOutput ->
                                            log.info("buy order placed, response: {}", addOrderOutput)
                                          );
                        case DO_NOTHING: log.info("no sell no buy decision");
                    }
                    return Flux.empty();
                }).subscribe(o -> {});
    }

    private Flux<AddOrderOutput> placeSellOrder(final TickerOutput tickerOutput) {
        log.info( "starting process for placing sell order based on ticker output: {}", tickerOutput );

        return
            exchangeService
                .getAvailableAssetForSellPlacement(XzAsset.XLTC, Asset.LTC)
                .filter  (availableAsset -> canPlaceSellOrder(availableAsset))
                .flatMap (availableAsset ->
                    exchangeService.addOrder(
                        AddOrderInput
                            .builder     ()
                            .assetPair   (
                                AssetPair
                                    .builder  ()
                                    .from     (XzAsset.XLTC)
                                    .to       (XzAsset.ZEUR)
                                    .build    ()
                                )
                            .addOrderType(AddOrderType.SELL)
                            .orderType   (OrderType.LIMIT)
                            .price       (tickerOutput.getAsk().getPrice())
                            .volume      (availableAsset)
                            .build()
                    )
                );
    }

    private Flux<AddOrderOutput> placeBuyOrder(final TickerOutput tickerOutput) {
        log.info( "starting process for placing buy order based on ticker output: {}", tickerOutput );

        return
                exchangeService
                    .getAvailableAssetForBuyPlacement(XzAsset.ZEUR, Asset.EUR)
                    .filter  (availableAsset -> canPlaceBuyOrder(availableAsset))
                    .flatMap (availableAsset ->
                        exchangeService
                            .addOrder(
                                AddOrderInput
                                    .builder     ()
                                    .assetPair   (
                                        AssetPair
                                            .builder  ()
                                            .from     (XzAsset.XLTC)
                                            .to       (XzAsset.ZEUR)
                                            .build    ()
                                    )
                                    .addOrderType(AddOrderType.BUY)
                                    .orderType   (OrderType.MARKET)
                                    .volume      ( availableAsset )
                                    .build()
                        )
                    );
    }

    private boolean canPlaceSellOrder (final BigDecimal availableAsset){
        if ( availableAsset.compareTo(AssetMinimumOrder.LITECOIN) <= 0 ) {
            log.info
            (
                "can not place sell order, available asset: {} {}, minimum order amount is: {} {}",
                availableAsset, Asset.LTC, AssetMinimumOrder.LITECOIN, Asset.LTC
            );
            return false;
        }
        log.info("can place sell order, available asset: {} {}", availableAsset, Asset.LTC);
        return true;
    }

    private boolean canPlaceBuyOrder (final BigDecimal availableAsset){
        if ( availableAsset.compareTo(AssetMinimumOrder.EURO) <= 0 ) {
            log.info
            (
                "can not place buy order, available asset: {} {}, minimum order amount is: {} {}",
                availableAsset, Asset.EUR, AssetMinimumOrder.EURO, Asset.EUR
            );
            return false;
        }
        log.info("can place buy order, available asset: {} {}", availableAsset, Asset.EUR);
        return true;
    }


}
