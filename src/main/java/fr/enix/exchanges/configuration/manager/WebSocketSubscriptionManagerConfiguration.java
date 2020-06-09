package fr.enix.exchanges.configuration.manager;

import fr.enix.exchanges.manager.ChannelManager;
import fr.enix.exchanges.manager.ConnectionManager;
import fr.enix.exchanges.manager.TickerResponseManager;
import fr.enix.exchanges.manager.WebSocketSubscriptionFactory;
import fr.enix.exchanges.repository.ChannelRepository;
import fr.enix.exchanges.service.TickerService;
import fr.enix.mapper.TickerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketSubscriptionManagerConfiguration {

    @Bean
    public ChannelManager channelManager(final ChannelRepository channelRepository) {
        return new ChannelManager(channelRepository);
    }

    @Bean
    public ConnectionManager connectionManager() {
        return new ConnectionManager();
    }

    @Bean
    public TickerResponseManager tickerResponseManager(final TickerService tickerService,
                                                       final TickerMapper tickerMapper) {
        return new TickerResponseManager(tickerService, tickerMapper);
    }

    @Bean
    public WebSocketSubscriptionFactory webSocketSubscriptionFactory(final ChannelManager channelManager,
                                                                     final ConnectionManager connectionManager,
                                                                     final TickerResponseManager tickerResponseManager) {
        return new WebSocketSubscriptionFactory(channelManager, connectionManager, tickerResponseManager);
    }
}
