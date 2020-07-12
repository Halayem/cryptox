package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.TickerService;
import fr.enix.exchanges.service.TransactionDecisionService;
import fr.enix.exchanges.service.impl.TickerTradeFixedThresholdServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TickerServiceConfiguration {

    @Bean
    public TickerService tickerService(final ExchangeService            exchangeService,
                                       final MarketOfferService         marketOfferService,
                                       final TransactionDecisionService transactionDecisionService) {

        return new TickerTradeFixedThresholdServiceImpl(exchangeService, marketOfferService, transactionDecisionService);
    }

}
