package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.service.*;
import fr.enix.exchanges.service.impl.TickerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TickerServiceConfiguration {

    @Bean
    public TickerService tickerService(final ExchangeService                    exchangeService,
                                       final TradingDecisionService             tradingDecisionService,
                                       final MarketOfferService                 marketOfferService,
                                       final CurrenciesRepresentationService    currenciesRepresentationService,
                                       final TickerMapper                       tickerMapper,
                                       final PriceReferenceService              priceReferenceService,
                                       final AddOrderMapper                     addOrderMapper) {
        return
            new TickerServiceImpl(
                    exchangeService,
                    tradingDecisionService,
                    marketOfferService,
                    currenciesRepresentationService,
                    tickerMapper,
                    priceReferenceService,
                    addOrderMapper
            );
    }
}
