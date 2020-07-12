package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.MarketPriceHistory;
import fr.enix.exchanges.model.ws.AssetPair;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import fr.enix.exchanges.service.MarketOfferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
@Slf4j
public class MarketOfferServiceImpl implements MarketOfferService {

    private final MarketOfferHistoryRepository marketOfferHistoryRepository;

    @Override
    public void resetAllMarketOfferHistory() {
        log.warn("market history data will be erased");
        marketOfferHistoryRepository.resetAllMarketOfferHistory();
    }

    @Override
    public Mono<MarketPriceHistory> saveNewMarketPrice(final AssetPair assetPair, final BigDecimal price) {
        log.info("market place, new price will be saved: {} {}", price, assetPair);
        return marketOfferHistoryRepository.saveNewMarketOffer(assetPair, price);
    }

}
