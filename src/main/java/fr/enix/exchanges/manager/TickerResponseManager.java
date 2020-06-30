package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.mapper.TickerMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TickerResponseManager implements WebSocketSubscriptionManager  {

    private final TickerService tickerService;
    private final TickerMapper tickerMapper;

    @Override
    public void managePayload(final String payload) throws JsonProcessingException {
        tickerService.marketOfferUpdateHandler(
            tickerMapper.mapTickerResponseToTickerOutput(
                tickerMapper.mapStringToTickerResponse(payload)
            )
        );
    }
}
