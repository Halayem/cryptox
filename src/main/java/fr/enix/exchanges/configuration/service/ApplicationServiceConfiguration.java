package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.repository.ExchangeRepository;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.repository.TickerHistoryRepository;
import fr.enix.exchanges.service.*;
import fr.enix.exchanges.service.impl.*;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecisionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfiguration {

    @Bean
    public ExchangeService exchangeService(final ExchangeRepository exchangeRepository) {
        return new ExchangeServiceImpl(exchangeRepository);
    }

    @Bean
    public MarketOfferService marketOfferService(final MarketPlaceService marketPlaceService,
                                                 final TickerHistoryRepository tickerHistoryRepository) {
        return new MarketOfferServiceImpl(marketPlaceService, tickerHistoryRepository);
    }

    @Bean
    public TradingDecisionService tradingBearingStrategyDecisionServiceImpl(final TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactory) {
        return new TradingBearingStrategyDecisionServiceImpl(tradingBearingStrategyDecisionFactory);
    }

    @Bean
    public PriceReferenceService priceReferenceService(final PriceReferenceRepository priceReferenceRepository) {
        return new PriceReferenceServiceImpl(priceReferenceRepository);
    }

    @Bean
    public TickerService tickerService(final ExchangeService exchangeService,
                                       final TradingDecisionService tradingDecisionService,
                                       final MarketOfferService marketOfferService,
                                       final CurrenciesRepresentationService currenciesRepresentationService,
                                       final TickerMapper tickerMapper,
                                       final AddOrderMapper addOrderMapper) {
        return
            new TickerServiceImpl(
                    exchangeService,
                    tradingDecisionService,
                    marketOfferService,
                    currenciesRepresentationService,
                    tickerMapper,
                    addOrderMapper
            );
    }
}
