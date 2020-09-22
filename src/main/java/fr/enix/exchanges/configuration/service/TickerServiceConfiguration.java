package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.service.impl.TickerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TickerServiceConfiguration {

    @Bean
    public TickerService tickerService(final MarketOfferService marketOfferService,
                                       final CurrenciesRepresentationService currenciesRepresentationService,
                                       final TickerMapper tickerMapper) {

        return new TickerServiceImpl(marketOfferService,
                                     currenciesRepresentationService,
                                     tickerMapper
       );
    }
}
