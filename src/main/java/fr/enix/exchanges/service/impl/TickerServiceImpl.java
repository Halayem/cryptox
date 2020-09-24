/**
 * for minimum trading order size, refers to https://support.kraken.com/hc/en-us/articles/205893708-Minimum-order-size-volume-for-trading#:~:text=For%20more%20details%2C%20see%20the%20Price%20%26%20Volume%20Decimal%20Precision%20article.&text=If%20you%20are%20trading%20XBT,be%2010%20EUR%20or%20larger.
 */
package fr.enix.exchanges.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.RoundingMode;

@Slf4j
@AllArgsConstructor
public class TickerServiceImpl implements TickerService {

    /*
    private final ExchangeService               exchangeService;
    private final TradingDecisionService    transactionDecisionService;
    private final AssetOrderIntervalRepository assetOrderIntervalRepository;
    private final int           DIVIDE_SCALE    = 8;
    private final RoundingMode  ROUNDING_MODE   = RoundingMode.DOWN;
    */

    private final MarketOfferService                marketOfferService;
    private final CurrenciesRepresentationService   currenciesRepresentationService;
    private final TickerMapper                      tickerMapper;



    @Override
    public void marketOfferUpdateHandler(final String payload) throws JsonProcessingException {

        tickerMapper.mapTickerResponseToTickerOutput(
            tickerMapper.mapStringToTickerResponse(payload)
        ).map( tickerOutput ->
                marketOfferService.saveApplicationAssetPairTicker(
                    currenciesRepresentationService.getApplicationAssetPairCurrencyRepresentationByMarketAssetPair(tickerOutput.getAssetPair()),
                        tickerOutput.getAsk().getPrice()
                )
        ).then();

        log.warn("ticker service has only saved new market offer information");
        /*
        final TickerOutput tickerOutput;
        marketOfferService.saveNewMarketPrice   (tickerOutput.getAssetPair(), tickerOutput.getAsk().getPrice())
                          .flatMap              (transactionDecisionService::getDecision)
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

         */
    }

    /*
    private Flux<AddOrderOutput> placeBuyOrder(final TickerOutput tickerOutput) {
        log.info( "starting process for placing buy order based on ticker output: {}", tickerOutput );

        return
            exchangeService.getAvailableAssetForBuyPlacement(XzAsset.ZEUR, Asset.EUR)
                    .filter  (this::canPlaceBuyOrder)
                    .flatMap (availableAsset ->
                            exchangeService.addOrder(
                                    AddOrderInput.builder     ()
                                                 .assetPair   (AssetPair.builder  ()
                                                                        .from     (XzAsset.XLTC)
                                                                        .to       (XzAsset.ZEUR)
                                                                        .build    ()
                                                 )
                                                 .addOrderType(AddOrderType.BUY)
                                                 .orderType   (OrderType.LIMIT)
                                                 .price       (tickerOutput.getBid().getPrice())
                                                 .volume      ( computeBuyVolume(availableAsset, tickerOutput.getBid().getPrice()))
                                                 .build()
                            )
                    );
    }

    protected BigDecimal computeBuyVolume(final BigDecimal availableAsset, final BigDecimal askPrice) {
        return availableAsset.compareTo(EURO_TRADING_VOLUME_UNIT) < 0
               ? availableAsset.divide          (askPrice, DIVIDE_SCALE, ROUNDING_MODE)
               : EURO_TRADING_VOLUME_UNIT.divide(askPrice, DIVIDE_SCALE, ROUNDING_MODE);
    }

    private boolean canPlaceBuyOrder (final BigDecimal availableAsset){
        if ( availableAsset.compareTo(assetOrderIntervalRepository.getMinimumEuroOrder()) <= 0 ) {
            log.info
            (
                "can not place buy order, available asset: {} {}, minimum order amount is: {} {}",
                 availableAsset, Asset.EUR, assetOrderIntervalRepository.getMinimumEuroOrder(), Asset.EUR
            );
            return false;
        }
        log.info("can place buy order, available asset: {} {}", availableAsset, Asset.EUR);
        return true;
    }

    private Flux<AddOrderOutput> placeSellOrder(final TickerOutput tickerOutput) {
        log.info( "starting process for placing sell order based on ticker output: {}", tickerOutput );

        return
        exchangeService.getAvailableAssetForSellPlacement(XzAsset.XLTC, Asset.LTC)
                       .filter  (this::canPlaceSellOrder)
                       .flatMap (availableAsset ->
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
                                                 .volume      ( availableAsset.compareTo(LITECOIN_TRADING_VOLUME_UNIT) <= 0
                                                                ? availableAsset
                                                                : LITECOIN_TRADING_VOLUME_UNIT
                                                 )
                                                 .build()
                                )
                       );
    }

    private boolean canPlaceSellOrder (final BigDecimal availableAsset){
        if ( availableAsset.compareTo(assetOrderIntervalRepository.getMinimumLitecoinOrder()) <= 0 ) {
            log.info
            (
                "can not place sell order, available asset: {} {}, minimum order amount is: {} {}",
                availableAsset, Asset.LTC, assetOrderIntervalRepository.getMinimumLitecoinOrder(), Asset.LTC
            );
            return false;
        }
        log.info("can place sell order, available asset: {} {}", availableAsset, Asset.LTC);
        return true;
    }


     */
}
