package fr.enix.exchanges.service;

import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.business.output.TickerOutput;
import reactor.core.publisher.Flux;

public interface TickerService {
    void marketOfferUpdateHandler(final TickerOutput tickerOutput);
}
