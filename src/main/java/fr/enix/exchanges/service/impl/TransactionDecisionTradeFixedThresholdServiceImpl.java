package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.repository.FixedThresholdRepository;
import fr.enix.exchanges.service.TransactionDecisionService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TransactionDecisionTradeFixedThresholdServiceImpl implements TransactionDecisionService  {

    private final FixedThresholdRepository fixedThresholdRepository;

    @Override
    public Mono<Decision> getDecision(MarketPriceHistory marketPriceHistory) {
        return
                fixedThresholdRepository.getThresholds()
                .flatMap(threshold -> {
                    return marketPriceHistory.getCurrentMarketOffer()
                                             .getPrice()
                                             .compareTo(threshold.getThresholdToBuy()) <= 0
                                             ? Mono.just(Decision.BUY)
                                             : marketPriceHistory.getCurrentMarketOffer()
                                                                 .getPrice()
                                                                 .compareTo(threshold.getThresholdToSell()) >= 0
                                                                 ? Mono.just(Decision.SELL)
                                                                 : Mono.just(Decision.DO_NOTHING); });
    }
}
