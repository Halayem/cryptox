package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface WebSocketSubscriptionManager {
    void managePayload(final String payload) throws JsonProcessingException;
}
