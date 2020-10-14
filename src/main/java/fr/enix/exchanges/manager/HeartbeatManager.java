package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.repository.HeartbeatRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class HeartbeatManager implements WebSocketSubscriptionManager {

    private final HeartbeatRepository heartbeatRepository;

    @Override
    public void managePayload(String payload) throws JsonProcessingException {
        log.debug("received payload: {}", payload);
        heartbeatRepository.saveHeartbeatDatetime();
    }

}
