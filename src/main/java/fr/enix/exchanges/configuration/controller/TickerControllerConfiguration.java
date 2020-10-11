package fr.enix.exchanges.configuration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.exchanges.event.WebSocketClientConnectionTerminatedEvent;
import fr.enix.exchanges.manager.WebSocketSubscriptionFactory;
import fr.enix.exchanges.model.ExchangeProperties;
import fr.enix.exchanges.model.parameters.KrakenPingProperties;
import fr.enix.exchanges.model.websocket.request.TickerRequest;
import fr.enix.exchanges.service.ApplicationTradingConfigurationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({
        ExchangeProperties.class,
        KrakenPingProperties.class
})
public class TickerControllerConfiguration {

    private final ApplicationTradingConfigurationService    applicationTradingConfigurationService;
    private final ApplicationEventPublisher                 applicationEventPublisher;

    @Bean
    public String tickerSubscriptionMessage() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(
                TickerRequest.builder  ()
                        .event         ("subscribe")
                        .pair          ( applicationTradingConfigurationService
                                .getEnabledAssetPairsRepresentationForWebSocketTrading()
                                .collectList()
                                .block() )
                        .subscription  ( TickerRequest.Subscription.builder().name("ticker").build() )
                        .build         ()
        );
    }

    @Bean
    public Consumer<String> tickerConsumer(final WebSocketSubscriptionFactory webSocketSubscriptionFactory) {
        return payload -> {
            try {
                webSocketSubscriptionFactory
                        .getWebSocketSubscriptionManager(payload)
                        .managePayload                  (payload);
            } catch (JsonProcessingException e) {
                log.error(
                        "json processing error, input string: {}, exception message: {}, complete stack trace: {}",
                        payload, e.getMessage(), e
                );
            }
        };
    }

    private final long webSocketSessionReceiveTimeoutInSeconds = 5l;

    @Bean
    public WebSocketHandler webSocketHandler(final String               tickerSubscriptionMessage,
                                             final Consumer             tickerConsumer,
                                             final KrakenPingProperties krakenPingProperties) {

        return  webSocketSession ->
                webSocketSession
                    .send(
                        Flux
                        .interval   (Duration.ofSeconds(krakenPingProperties.getFrequency())        )
                        .map        (pingPayload -> krakenPingProperties.getPayload()               )
                        .doOnNext   (pingPayload -> log.debug("sending ping request {}", pingPayload))
                        .map        (webSocketSession::textMessage                                  )
                    ).mergeWith(
                        webSocketSession.send( Mono.just( webSocketSession.textMessage(tickerSubscriptionMessage) ) )
                    ).mergeWith(
                        webSocketSession
                            .receive    ()
                            .timeout    (Duration.ofSeconds(webSocketSessionReceiveTimeoutInSeconds))
                            .map        (WebSocketMessage::getPayloadAsText)
                            .doOnNext   (tickerConsumer)
                            .doOnTerminate(() -> {
                                applicationEventPublisher.publishEvent(
                                    new WebSocketClientConnectionTerminatedEvent(webSocketClientTimeoutMessage()));
                            })
                            .then       ()
                    )
                    .then();
    }

    @Bean
    public Mono<Void> tickerWebSocketClient(final ExchangeProperties    exchangeProperties,
                                            final WebSocketHandler      webSocketHandler) {
        return
            new ReactorNettyWebSocketClient()
                .execute(
                    URI.create(exchangeProperties.getUrl().get("websocket")),
                    webSocketHandler
                );
    }

    private String webSocketClientTimeoutMessage() {
        return String.format(
                "web socket session client is now terminated, timeout of <%d> seconds without receiving input maybe reached",
                webSocketSessionReceiveTimeoutInSeconds
        );
    }
}
