package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.output.TickerOutput;

public interface TickerService {
    void marketOfferUpdateHandler(final TickerOutput tickerOutput);
}
