package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.service.TransactionDecisionService;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class TransactionDecisionTradeFixedThresholdServiceImpl implements TransactionDecisionService  {
    private final BigDecimal thresholdPriceToBuy    = new BigDecimal(38);
    private final BigDecimal thresholdPriceToSell   = new BigDecimal(40);

    @Override
    public Mono<Decision> getDecision(MarketPriceHistory marketPriceHistory) {
        return marketPriceHistory.getCurrentMarketOffer().getPrice().compareTo(thresholdPriceToBuy) <= 0
                ? Mono.just(Decision.BUY)
                : marketPriceHistory.getCurrentMarketOffer().getPrice().compareTo(thresholdPriceToSell) >= 0
                    ? Mono.just(Decision.SELL)
                    : Mono.just(Decision.DO_NOTHING);
    }
}
