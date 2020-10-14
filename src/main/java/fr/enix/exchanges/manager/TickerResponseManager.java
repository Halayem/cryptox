package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.service.TickerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class TickerResponseManager implements WebSocketSubscriptionManager {

    private final TickerService tickerService;

    @Override
    public void managePayload(final String payload) throws JsonProcessingException {
        log.debug("received payload: {}", payload);
        tickerService.marketOfferUpdateHandler(payload).subscribe(
            addOrderOutput -> log.info("order placed successfully, response: {}", addOrderOutput)
        );
    }
}
