package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartbeatManager implements WebSocketSubscriptionManager {

    @Override
    public void managePayload(String payload) throws JsonProcessingException {
        log.info("received heartbeat payload: {}", payload);
    }
}
