package fr.enix.exchanges.configuration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.manager.WebSocketSubscriptionFactory;
import fr.enix.exchanges.model.ExchangeProperties;
import fr.enix.exchanges.model.ws.AssetPair;
import fr.enix.exchanges.model.parameters.Asset;
import fr.enix.exchanges.mapper.AssetMapper;
import fr.enix.exchanges.service.ApplicationTradingConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Configuration
@EnableConfigurationProperties( ExchangeProperties.class )
public class TickerControllerConfiguration {

    private final TickerControllerConfigurationHelper tickerControllerConfigurationHelper;
    private final ApplicationTradingConfigurationService applicationTradingConfigurationService;
    private final AssetMapper assetMapper;

    public TickerControllerConfiguration(final AssetMapper assetMapper,
                                         final ApplicationTradingConfigurationService applicationTradingConfigurationService) {
        this.assetMapper = assetMapper;
        this.tickerControllerConfigurationHelper = new TickerControllerConfigurationHelper();
        this.applicationTradingConfigurationService = applicationTradingConfigurationService;
    }

    /*
    @Bean
    public Mono<Void> litecoinToEuroTickerWebSocketClient(final ExchangeProperties  exchangeProperties,
                                                          final String              litecoinToEuroTickerSubscriptionMessage,
                                                          final Consumer<String>    litecoinToEuroTickerConsumer) {

        return
            new ReactorNettyWebSocketClient()
                .execute(
                    URI.create(exchangeProperties.getUrl().get("websocket")),
                    tickerControllerConfigurationHelper.getNewWebSocketHandler(
                        litecoinToEuroTickerSubscriptionMessage,
                        litecoinToEuroTickerConsumer
                    )
                );
    }

    @Bean
    public Consumer<String> litecoinToEuroTickerConsumer(final WebSocketSubscriptionFactory webSocketSubscriptionFactory) {
        return payload -> {
            try {
                webSocketSubscriptionFactory.getWebSocketSubscriptionManager(payload)
                                            .managePayload                  (payload);
            } catch (JsonProcessingException e) {
                log.error(
                    "json processing error, input string: {}, exception message: {}, complete stack trace: {}",
                    payload, e.getMessage(), e
                );
            }
        };
    }

    @Bean
    public String litecoinToEuroTickerSubscriptionMessage(List<String> assetPairs) throws JsonProcessingException {
        return tickerControllerConfigurationHelper.getNewTickerSubscriptionMessage(
                applicationTradingConfigurationService
                .getAssetPairsToSubscribe()
        );
    }

     */

}
