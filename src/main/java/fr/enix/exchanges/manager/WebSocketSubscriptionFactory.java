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
    private final WebSocketSubscriptionManager pongManager;

    public WebSocketSubscriptionManager getWebSocketSubscriptionManager(final String payload) {
        log.debug("string payload, before calling the adequate manager: {}", payload);

        if ( isHeartbeatPayload (payload) ) { return heartbeatManager;      }
        if ( isTickerPayload    (payload) ) { return tickerResponseManager; }
        if ( isPongPayload      (payload) ) { return pongManager;           }
        if ( isChannelPayload   (payload) ) { return channelManager;        }
        if ( isConnectionPayload(payload) ) { return connectionManager;     }

        throw new RuntimeException("no manager configured for this payload: " + payload);
    }

    private boolean isHeartbeatPayload  (final String payload) { return payload.contains("heartbeat");          }
    private boolean isPongPayload       (final String payload) { return payload.contains("pong");               }
    private boolean isTickerPayload     (final String payload) { return payload.matches("^\\[(\\d+).*"); }
    private boolean isConnectionPayload (final String payload) { return payload.indexOf("connectionID" ) == 2;  }
    private boolean isChannelPayload    (final String payload) { return payload.indexOf("channelID") == 2;      }

}
