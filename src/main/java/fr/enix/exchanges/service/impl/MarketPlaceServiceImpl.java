package fr.enix.exchanges.service.impl;

import fr.enix.common.MarketPlace;
import fr.enix.exchanges.service.MarketPlaceService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MarketPlaceServiceImpl implements MarketPlaceService {
    private final MarketPlace marketPlace;
}
