package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.common.exception.websocket.KrakenWebSocketConnectionException;
import fr.enix.exchanges.model.websocket.response.ConnectionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionManager implements WebSocketSubscriptionManager {

    @Override
    public void managePayload(final String payload) throws JsonProcessingException {

        final ConnectionResponse connectionResponse = new ObjectMapper().readValue(payload, ConnectionResponse.class);
        if( ! "online".equals(connectionResponse.getStatus() ) ) {
            throw new KrakenWebSocketConnectionException("status is not online, original payload received: " + payload );
        }

        log.info(buildLogMessage(connectionResponse));

    }

    private String buildLogMessage(final ConnectionResponse connectionResponse) {
        return new StringBuilder("web socket connection established, connection information:").append("\n\t")
                    .append("- connection --- <").append(connectionResponse.getConnectionID()   ).append(">").append("\n\t")
                    .append("- event -------- <").append(connectionResponse.getEvent()          ).append(">").append("\n\t")
                    .append("- status ------- <").append(connectionResponse.getStatus()         ).append(">").append("\n\t")
                    .append("- version ------ <").append(connectionResponse.getVersion()        ).append(">")
                    .toString();
    }
}
