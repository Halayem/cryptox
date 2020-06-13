package fr.enix.exchanges.configuration.mapper;

import fr.enix.mapper.AddOrderMapper;
import fr.enix.mapper.TickerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public AddOrderMapper addOrderMapper() {
        return new AddOrderMapper();
    }

    @Bean
    public TickerMapper tickerMapper() { return new TickerMapper(); }
}
