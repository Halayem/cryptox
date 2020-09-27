package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.service.*;
import fr.enix.exchanges.service.impl.TickerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TickerServiceConfiguration {

    @Bean
    public TickerService tickerService(final TradingDecisionService tradingDecisionService,
                                       final MarketOfferService marketOfferService,
                                       final CurrenciesRepresentationService currenciesRepresentationService,
                                       final TickerMapper tickerMapper,
                                       final PriceReferenceService priceReferenceService) {
        return
            new TickerServiceImpl(
                    tradingDecisionService,
                    marketOfferService,
                    currenciesRepresentationService,
                    tickerMapper,
                    priceReferenceService
            );
    }
}
