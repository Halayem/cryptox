package fr.enix.exchanges.repository;

import fr.enix.common.MarketPlace;
import fr.enix.exchanges.model.business.MarketOfferHistorySearchRequest;
import fr.enix.exchanges.model.business.MarketOfferHistorySearchResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MarketOfferHistoryRepository {

    Mono<Void> saveApplicationAssetPairTicker(final MarketPlace marketPlace, final String applicationAssetPair, final BigDecimal price);
    Mono<MarketOfferHistorySearchResponse> getHighestOffer(final MarketOfferHistorySearchRequest marketOfferHistorySearchRequest);
    Mono<MarketOfferHistorySearchResponse> getLowestOffer(final MarketOfferHistorySearchRequest marketOfferHistorySearchRequest);
}
