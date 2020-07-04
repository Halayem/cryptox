package fr.enix.exchanges.service.impl;


import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.TickerOutput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.parameters.OrderType;
import fr.enix.exchanges.model.parameters.XzAsset;
import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.websocket.AssetPair;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.service.TransactionDecisionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
public class TickerServiceImpl implements TickerService {

    private final ExchangeService               exchangeService;
    private final MarketOfferService            marketOfferService;
    private final TransactionDecisionService    transactionDecisionService;

    private final BigDecimal LITECOIN_VOLUME_UNIT = new BigDecimal("0.5");

    @Override
    public void marketOfferUpdateHandler(final TickerOutput tickerOutput) {

        marketOfferService.saveNewMarketPrice   (tickerOutput.getAssetPair(), tickerOutput.getAsk().getPrice())
                          .flatMap              (marketPriceHistory -> transactionDecisionService.getDecision(marketPriceHistory))
                          .map                  (decision -> {
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

    private Flux<AddOrderOutput> placeBuyOrder(final TickerOutput tickerOutput) {
        log.info( "placing buy order based on ticker output: {}", tickerOutput );
        return Flux.empty();
    }

    private Flux<AddOrderOutput> placeSellOrder(final TickerOutput tickerOutput) {
        log.info( "placing sell order based on ticker output: {}", tickerOutput );

        return
        exchangeService.getAvailableAssetForSellPlacement(XzAsset.XLTC, Asset.LTC)
                       .flatMap(availableAsset ->
                                exchangeService.addOrder(
                                    AddOrderInput.builder     ()
                                                 .assetPair   (AssetPair.builder  ()
                                                                        .from     (XzAsset.XLTC)
                                                                        .to       (XzAsset.ZEUR)
                                                                        .build    ()
                                                 )
                                                 .addOrderType(AddOrderType.SELL)
                                                 .orderType   (OrderType.LIMIT)
                                                 .price       (tickerOutput.getAsk().getPrice())
                                                 .volume      ( availableAsset.compareTo(LITECOIN_VOLUME_UNIT) <= 0
                                                                ? availableAsset
                                                                : LITECOIN_VOLUME_UNIT
                                                 )
                                                 .build()
                                )
                       );
    }

}
