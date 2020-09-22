package fr.enix.exchanges.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.model.business.output.TickerOutput;

public interface TickerService {
    void marketOfferUpdateHandler(final String payload) throws JsonProcessingException;
}
