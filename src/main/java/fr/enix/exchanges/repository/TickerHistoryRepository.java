package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.business.MarketOfferHistorySearchRequest;
import fr.enix.exchanges.model.repository.Ticker;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface TickerHistoryRepository extends ReactiveCrudRepository<Ticker, Long> {

    @Query ("SELECT MAX(PRICE) FROM TICKER_HISTORY "                                            +
            "WHERE MARKET   = :marketOfferHistorySearchRequest.marketPlace "            +
            "AND ASSET_PAIR = :marketOfferHistorySearchRequest.applicationAssetPair "   +
            "AND AT > :marketOfferHistorySearchRequest.after"                           +
            "AND AT < :marketOfferHistorySearchRequest.before")
    Mono<BigDecimal> getHighestPrice(final MarketOfferHistorySearchRequest marketOfferHistorySearchRequest);

    @Query ("SELECT MIN(PRICE) FROM TICKER_HISTORY "                                            +
            "WHERE MARKET   = :marketOfferHistorySearchRequest.marketPlace "            +
            "AND ASSET_PAIR = :marketOfferHistorySearchRequest.applicationAssetPair "   +
            "AND AT > :marketOfferHistorySearchRequest.after"                           +
            "AND AT < :marketOfferHistorySearchRequest.before")
    Mono<BigDecimal> getLowestPrice(final MarketOfferHistorySearchRequest marketOfferHistorySearchRequest);
}
