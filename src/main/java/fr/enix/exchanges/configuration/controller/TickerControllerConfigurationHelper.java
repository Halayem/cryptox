package fr.enix.exchanges.configuration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.exchanges.model.websocket.request.TickerRequest;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Consumer;

public class TickerControllerConfigurationHelper {

    protected String getNewTickerSubscriptionMessage(final String assetPair) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(
                TickerRequest.builder  ()
                        .event         ("subscribe")
                        .pair          ( Arrays.asList( assetPair ) )
                        .subscription  ( TickerRequest.Subscription.builder().name("ticker").build() )
                        .build         ()
        );
    }

    protected WebSocketHandler getNewWebSocketHandler(final String subscriptionMessage,
                                                      final Consumer consumer) {
        return  webSocketSession ->
                    webSocketSession.send       (Mono.just( webSocketSession.textMessage(subscriptionMessage) ))

                                    .thenMany   (
                                        webSocketSession.receive  ()
                                                .map      (WebSocketMessage::getPayloadAsText)
                                                .filter   ( payload -> payload.contains("heartbeat") == false )
                                                .doOnNext ( consumer )
                                    )
                                    .then();
    }
}
