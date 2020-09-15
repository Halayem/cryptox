package fr.enix.exchanges.manager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class WebSocketSubscriptionFactory {

    private final WebSocketSubscriptionManager channelManager;
    private final WebSocketSubscriptionManager connectionManager;
    private final WebSocketSubscriptionManager tickerResponseManager;
    private final WebSocketSubscriptionManager heartbeatManager;

    public WebSocketSubscriptionManager getWebSocketSubscriptionManager(final String payload) {
        log.info("string payload, before calling the adequate manager: {}", payload);

        if ( isHeartbeatPayload (payload) ) { return heartbeatManager;      }
        if ( isConnectionPayload(payload) ) { return connectionManager;     }
        if ( isChannelPayload   (payload) ) { return channelManager;        }
        if ( isTickerPayload    (payload) ) { return tickerResponseManager; }

        throw new RuntimeException("no manager configured for this payload: " + payload);
    }

    private boolean isHeartbeatPayload  (final String payload) { return payload.contains("heartbeat");          }
    private boolean isConnectionPayload (final String payload) { return payload.indexOf("connectionID" ) == 2;  }
    private boolean isChannelPayload    (final String payload) { return payload.indexOf("channelID") == 2;      }
    private boolean isTickerPayload     (final String payload) { return payload.matches("^\\[(\\d+).*"); }

}
