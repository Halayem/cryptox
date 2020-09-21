package fr.enix.exchanges.configuration.manager;

import fr.enix.exchanges.manager.*;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.repository.HeartbeatRepository;
import fr.enix.exchanges.repository.PongRepository;
import fr.enix.exchanges.service.ChannelService;
import fr.enix.exchanges.service.TickerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketSubscriptionManagerConfiguration {

    @Bean
    public WebSocketSubscriptionManager channelManager(final ChannelService channelService) {
        return new ChannelManager(channelService);
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

    @Bean
    WebSocketSubscriptionManager heartbeatManager(final HeartbeatRepository heartbeatRepository) {
        return new HeartbeatManager(heartbeatRepository);
    }

    @Bean
    WebSocketSubscriptionManager pongManager(final PongRepository pongRepository) {
        return new PongManager(pongRepository);
    }

    @Bean
    public WebSocketSubscriptionFactory webSocketSubscriptionFactory(final WebSocketSubscriptionManager channelManager,
                                                                     final WebSocketSubscriptionManager connectionManager,
                                                                     final WebSocketSubscriptionManager tickerResponseManager,
                                                                     final WebSocketSubscriptionManager heartbeatManager,
                                                                     final WebSocketSubscriptionManager pongManager) {
        return new WebSocketSubscriptionFactory(
                channelManager,
                connectionManager,
                tickerResponseManager,
                heartbeatManager,
                pongManager
        );
    }


}
