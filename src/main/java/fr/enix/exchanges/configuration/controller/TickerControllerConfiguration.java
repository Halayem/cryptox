package fr.enix.exchanges.configuration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.exchanges.manager.WebSocketSubscriptionFactory;
import fr.enix.exchanges.model.ExchangeProperties;
import fr.enix.exchanges.model.websocket.request.TickerRequest;
import fr.enix.kraken.AssetPair;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;

@Configuration
@EnableConfigurationProperties( ExchangeProperties.class )
public class TickerControllerConfiguration {

    @Bean
    public String tickerSubscriptionMessage() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(
                TickerRequest.builder       ()
                             .event         ("subscribe")
                             .pair          ( Arrays.asList( AssetPair.LITECOIN_TO_EURO.getWsCode() ) )
                             .subscription  ( TickerRequest.Subscription.builder().name("ticker").build() )
                             .build         ()
        );
    }

    @Bean
    public WebSocketHandler tickerWebSocketHandler(final WebSocketSubscriptionFactory webSocketSubscriptionFactory,
                                                   final String tickerSubscriptionMessage) {
        return  webSocketSession ->
                    webSocketSession.send(
                        Mono.just       ( webSocketSession.textMessage(tickerSubscriptionMessage)))
                            .thenMany   (
                                webSocketSession.receive  ()
                                                .map      (WebSocketMessage::getPayloadAsText)
                                                .filter   ( payload -> payload.contains("heartbeat") == false )
                                                .doOnNext ( payload -> {
                                                    try {
                                                        webSocketSubscriptionFactory.getWebSocketSubscriptionManager(payload)
                                                                                    .managePayload                  (payload);
                                                    } catch (JsonProcessingException e) {
                                                        e.printStackTrace();
                                                    }
                                                })
                            )
                            .then();
    }

    @Bean
    public Mono<Void> tickerWebSocketClient(final WebSocketHandler tickerWebSocketHandler,
                                            final ExchangeProperties exchangeProperties) {

        return new ReactorNettyWebSocketClient()
                   .execute(
                        URI.create(exchangeProperties.getUrl().get("websocket")),
                        tickerWebSocketHandler
                   );
    }
}
