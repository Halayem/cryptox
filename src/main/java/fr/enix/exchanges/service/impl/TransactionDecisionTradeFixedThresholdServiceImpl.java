package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.repository.FixedThresholdRepository;
import fr.enix.exchanges.service.TransactionDecisionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
public class TransactionDecisionTradeFixedThresholdServiceImpl implements TransactionDecisionService  {

    private final FixedThresholdRepository fixedThresholdRepository;

    @Override
    public Mono<Decision> getDecision(MarketPriceHistory marketPriceHistory) {
        return
                fixedThresholdRepository.getThresholds()
                .flatMap(threshold -> {
                    log.info("*** current threshold: {}, current market price: {}", threshold, marketPriceHistory.getCurrentMarketOffer().getPrice());
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
