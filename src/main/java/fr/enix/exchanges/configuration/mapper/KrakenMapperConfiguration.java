package fr.enix.exchanges.configuration.mapper;

import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.mapper.impl.KrakenTickerMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("kraken")
public class KrakenMapperConfiguration {

    @Bean
    public TickerMapper tickerMapper() {
        return new KrakenTickerMapperImpl();
    }
}
