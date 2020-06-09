package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.TickerOutput;

public interface TickerService {
    void marketOfferUpdateHandler(final TickerOutput tickerOutput);
}
