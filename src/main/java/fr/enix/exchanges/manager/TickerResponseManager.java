package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.mapper.TickerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class TickerResponseManager implements WebSocketSubscriptionManager  {

    private final TickerService tickerService;
    private final TickerMapper tickerMapper;

    @Override
    public void managePayload(final String payload) throws JsonProcessingException {
        log.info("ticker response manager received payload: {}", payload);
        log.warn("implementation of ticker response manager is disabled");

        /*
        tickerService.marketOfferUpdateHandler(
            tickerMapper.mapTickerResponseToTickerOutput(
                tickerMapper.mapStringToTickerResponse(payload)
            )
        );
        */
    }
}
