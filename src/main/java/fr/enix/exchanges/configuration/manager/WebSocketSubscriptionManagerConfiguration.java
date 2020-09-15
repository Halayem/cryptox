package fr.enix.exchanges.configuration.manager;

import fr.enix.exchanges.manager.*;
import fr.enix.exchanges.repository.ChannelRepository;
import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.mapper.TickerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketSubscriptionManagerConfiguration {

    @Bean
    public WebSocketSubscriptionManager channelManager(final ChannelRepository channelRepository) {
        return new ChannelManager(channelRepository);
    }

    @Bean
    public WebSocketSubscriptionManager connectionManager() {
        return new ConnectionManager();
    }

    @Bean
    public WebSocketSubscriptionManager tickerResponseManager(final TickerService tickerService,
                                                              final TickerMapper tickerMapper) {
        return new TickerResponseManager(tickerService, tickerMapper);
    }

    @Bean WebSocketSubscriptionManager heartbeatManager() {
        return new HeartbeatManager();
    }

    @Bean
    public WebSocketSubscriptionFactory webSocketSubscriptionFactory(final WebSocketSubscriptionManager channelManager,
                                                                     final WebSocketSubscriptionManager connectionManager,
                                                                     final WebSocketSubscriptionManager tickerResponseManager,
                                                                     final WebSocketSubscriptionManager heartbeatManager) {
        return new WebSocketSubscriptionFactory(
                channelManager,
                connectionManager,
                tickerResponseManager,
                heartbeatManager
        );
    }
}
