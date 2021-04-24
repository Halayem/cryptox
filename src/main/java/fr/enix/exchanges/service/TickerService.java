package fr.enix.exchanges.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import reactor.core.publisher.Flux;

public interface TickerService {
    Flux<AddOrderOutput> marketOfferUpdateHandler(final String payload) throws JsonProcessingException;
}
