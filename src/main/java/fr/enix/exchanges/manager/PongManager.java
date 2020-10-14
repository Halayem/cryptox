package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.repository.PongRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class PongManager implements WebSocketSubscriptionManager {

    private final PongRepository pongRepository;

    @Override
    public void managePayload(String payload) throws JsonProcessingException {
        log.debug("received payload: {}", payload);
        pongRepository.savePongDatetime();
    }
}
