package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.TransactionDecisionService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TransactionDecisionServiceImpl implements TransactionDecisionService {

    private final MarketOfferService marketOfferService;

    @Override
    public Mono<Decision> getDecision(final Asset asset) {
        return marketOfferService.getMarketPriceHistory (asset)
                                 .flatMap               (marketPriceHistory -> getDecision(marketPriceHistory));
    }

    private Mono<Decision> getDecision(final MarketPriceHistory marketPriceHistory) {
        if( marketPriceHistory.getCurrentMarketOffer() == null || marketPriceHistory.getCurrentMarketOffer() == null) {
            return Mono.just(Decision.DO_NOTHING);
        }
        return compareCurrentAndPreviousPrice(marketPriceHistory) < 0
                ? Mono.just(Decision.BUY)
                : Mono.just(Decision.SELL);
    }

    private int compareCurrentAndPreviousPrice(final MarketPriceHistory marketPriceHistory) {
        return marketPriceHistory.getCurrentMarketOffer().getPrice().compareTo(
                marketPriceHistory.getPreviousMarketOffer().getPrice()
        );
    }

}
