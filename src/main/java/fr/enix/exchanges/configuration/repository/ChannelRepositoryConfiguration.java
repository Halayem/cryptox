package fr.enix.exchanges.configuration.repository;

import fr.enix.exchanges.repository.ChannelRepository;
import fr.enix.exchanges.repository.impl.ChannelRepositoryInMemoryRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelRepositoryConfiguration {

    @Bean
    public ChannelRepository channelRepository() {
        return new ChannelRepositoryInMemoryRepositoryImpl();
    }
}
