package fr.enix.exchanges.repository.impl;

import fr.enix.common.MarketPlace;
import fr.enix.exchanges.model.business.MarketOfferHistorySearchRequest;
import fr.enix.exchanges.model.business.MarketOfferHistorySearchResponse;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@NoArgsConstructor
@Slf4j
public class MarketOfferHistoryRepositoryImpl implements MarketOfferHistoryRepository {


    @Override
    public Mono<Void> saveApplicationAssetPairTicker(final MarketPlace marketPlace, final String applicationAssetPair, final BigDecimal price) {
        log.debug("ticker will be saved, market place: {}, application asset pair {}, price: {}", marketPlace, applicationAssetPair, price);

        return  null;
    }

    @Override
    public Mono<MarketOfferHistorySearchResponse> getHighestOffer(MarketOfferHistorySearchRequest marketOfferHistorySearchRequest) {
        return null;
    }

    @Override
    public Mono<MarketOfferHistorySearchResponse> getLowestOffer(MarketOfferHistorySearchRequest marketOfferHistorySearchRequest) {
        return null;
    }

}
