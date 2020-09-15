package fr.enix.exchanges.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSocketSubscriptionFactoryTest {

    @Autowired
    private WebSocketSubscriptionFactory webSocketSubscriptionFactory;

    @Test
    void testGetWebSocketSubscriptionManager_shouldReturnHeartbeatManagerBean() {
        assertTrue(
                webSocketSubscriptionFactory.getWebSocketSubscriptionManager("{\"event\":\"heartbeat\"}")
                instanceof HeartbeatManager
        );
    }

    @Test
    void testGetWebSocketSubscriptionManager_shouldReturnConnectionManagerBean() {
        assertTrue(
                webSocketSubscriptionFactory.getWebSocketSubscriptionManager("{\"connectionID\":17816213703238877174,\"event\":\"systemStatus\",\"status\":\"online\",\"version\":\"1.2.0\"}")
                instanceof ConnectionManager
        );
    }

    @Test
    void testGetWebSocketSubscriptionManager_shouldReturnChannelManagerBean() {
        assertTrue(
                webSocketSubscriptionFactory.getWebSocketSubscriptionManager("{\"channelID\":379,\"channelName\":\"ticker\",\"event\":\"subscriptionStatus\",\"pair\":\"LTC/EUR\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ticker\"}}")
                instanceof ChannelManager
        );
    }

    @Test
    void testGetWebSocketSubscriptionManager_shouldReturnTickerResponseManagerBean() {
        assertTrue(
                webSocketSubscriptionFactory.getWebSocketSubscriptionManager("[379,{\"a\":[\"41.32000\",33,\"33.99166971\"],\"b\":[\"41.28000\",8,\"8.87172485\"],\"c\":[\"41.32000\",\"0.64017867\"],\"v\":[\"8036.82075470\",\"10107.32139774\"],\"p\":[\"41.69123\",\"41.59040\"],\"t\":[1041,1304],\"l\":[\"40.88000\",\"40.88000\"],\"h\":[\"42.40000\",\"42.40000\"],\"o\":[\"41.42000\",\"41.37000\"]},\"ticker\",\"LTC/EUR\"]")
                instanceof TickerResponseManager
        );
    }

    @Test
    void testGetWebSocketSubscriptionManager_shouldThrowRuntimeExceptionWhenPayloadIsNotManaged() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                webSocketSubscriptionFactory.getWebSocketSubscriptionManager("unknown payload")
        );

        assertEquals("no manager configured for this payload: unknown payload", exception.getMessage());
    }
}
