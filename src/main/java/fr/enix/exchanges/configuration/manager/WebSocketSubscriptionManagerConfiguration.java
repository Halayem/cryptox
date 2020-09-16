package fr.enix.exchanges.configuration.manager;

import fr.enix.exchanges.manager.*;
import fr.enix.exchanges.model.parameters.KrakenHeartbeatMonitorParameters;
import fr.enix.exchanges.repository.ChannelRepository;
import fr.enix.exchanges.repository.HeartbeatRepository;
import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.mapper.TickerMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KrakenHeartbeatMonitorParameters.class)
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

    @Bean WebSocketSubscriptionManager heartbeatManager(final HeartbeatRepository heartbeatRepository,
                                                        final KrakenHeartbeatMonitorParameters krakenHeartbeatMonitorParameters) {
        return new HeartbeatManager(heartbeatRepository, krakenHeartbeatMonitorParameters);
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
