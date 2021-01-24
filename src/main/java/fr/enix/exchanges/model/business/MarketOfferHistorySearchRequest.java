package fr.enix.exchanges.model.business;

import fr.enix.common.MarketPlace;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MarketOfferHistorySearchRequest {

    private MarketPlace marketPlace;
    private String applicationAssetPair;
    private LocalDateTime after;
    private LocalDateTime before;
}
