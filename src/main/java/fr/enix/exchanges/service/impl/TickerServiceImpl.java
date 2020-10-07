/**
 * for minimum trading order size, refers to https://support.kraken.com/hc/en-us/articles/205893708-Minimum-order-size-volume-for-trading#:~:text=For%20more%20details%2C%20see%20the%20Price%20%26%20Volume%20Decimal%20Precision%20article.&text=If%20you%20are%20trading%20XBT,be%2010%20EUR%20or%20larger.
 */
package fr.enix.exchanges.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class TickerServiceImpl implements TickerService {

    private final ExchangeService                   exchangeService;
    private final TradingDecisionService            tradingDecisionService;
    private final MarketOfferService                marketOfferService;
    private final CurrenciesRepresentationService   currenciesRepresentationService;
    private final TickerMapper                      tickerMapper;
    private final PriceReferenceService             priceReferenceService;
    private final AddOrderMapper                    addOrderMapper;

    @Override
    public Mono<AddOrderOutput> marketOfferUpdateHandler(final String payload) throws JsonProcessingException {
        return
            tickerMapper.mapTickerResponseToTickerOutput(tickerMapper.mapStringToTickerResponse(payload))
            .flatMap( tickerOutput ->
                marketOfferService.saveApplicationAssetPairTicker(
                    currenciesRepresentationService.getApplicationAssetPairCurrencyRepresentationByMarketAssetPair(tickerOutput.getAssetPair()),
                    tickerOutput.getAsk().getPrice()
                )
            )
            .flatMap( tradingDecisionService::getDecision)
            .flatMap( applicationAssetPairTickerTradingDecision -> {

                log.info(
                    "trading decision service has acted as follow: {}",
                    applicationAssetPairTickerTradingDecision.getFormattedLogMessage()
                );

                priceReferenceService.checkAndUpdatePriceReference(applicationAssetPairTickerTradingDecision);
                return placeOrder( applicationAssetPairTickerTradingDecision );
            });
    }

    private Mono<AddOrderOutput> placeOrder(final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision) {
        return
            Mono
            .just       ( applicationAssetPairTickerTradingDecision.getOperation().getDecision() )
            .filter     ( this::isSellOrBuyDecision )
            .flatMap    ( decision -> {
                switch (decision) {
                    case SELL:  return placeSellOrder  ( applicationAssetPairTickerTradingDecision );
                    case BUY:   return placeBuyOrder   ( applicationAssetPairTickerTradingDecision );
                }
                return Mono.empty();
            });
    }

    private boolean isSellOrBuyDecision(final ApplicationAssetPairTickerTradingDecision.Decision decision) {
        return  ApplicationAssetPairTickerTradingDecision.Decision.SELL.equals  (decision) ||
                ApplicationAssetPairTickerTradingDecision.Decision.BUY.equals   (decision);
    }


    protected Mono<AddOrderOutput> placeSellOrder( final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision ) {
        return
            newAddOrderInputForSellPlacement( applicationAssetPairTickerTradingDecision )
            .flatMap( exchangeService::addOrder);
    }

    protected Mono<AddOrderOutput> placeBuyOrder( final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision ) {
        return
            newAddOrderInputForBuyPlacement(applicationAssetPairTickerTradingDecision)
            .flatMap( exchangeService::addOrder );
    }

    protected Mono<AddOrderInput> newAddOrderInputForBuyPlacement(final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision) {
        return
            Mono.just   ( applicationAssetPairTickerTradingDecision.getAmount() )
            .flatMap    ( amountToBuy ->
                addOrderMapper.newAddOrderInputForBuyPlacement(
                    applicationAssetPairTickerTradingDecision.getApplicationAssetPairTickerReference().getApplicationAssetPair(),
                    amountToBuy,
                    applicationAssetPairTickerTradingDecision.getPrice()
                )
            );
    }

    protected Mono<AddOrderInput> newAddOrderInputForSellPlacement(final ApplicationAssetPairTickerTradingDecision applicationAssetPairTickerTradingDecision) {
        return
            Mono.just   ( applicationAssetPairTickerTradingDecision.getAmount() )
            .flatMap    ( amountToSell ->
                addOrderMapper.newAddOrderInputForSellPlacement(
                    applicationAssetPairTickerTradingDecision.getApplicationAssetPairTickerReference().getApplicationAssetPair(),
                    amountToSell,
                    applicationAssetPairTickerTradingDecision.getPrice()
                )
            );
    }
}
