package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.service.impl.TickerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TickerServiceConfiguration {

    @Bean
    public TickerService tickerService() {
        return new TickerServiceImpl();
    }

}
