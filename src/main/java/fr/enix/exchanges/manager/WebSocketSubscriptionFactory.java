package fr.enix.exchanges.manager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class WebSocketSubscriptionFactory {

    private final ChannelManager channelManager;
    private final ConnectionManager connectionManager;
    private final TickerResponseManager tickerResponseManager;

    public WebSocketSubscriptionManager getWebSocketSubscriptionManager(final String payload) {

        if ( payload.indexOf("connectionID" ) == 2) { return connectionManager; }
        if ( payload.indexOf("channelID"    ) == 2) { return channelManager;    }

        return tickerResponseManager;
    }

}
