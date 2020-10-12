package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.repository.*;
import fr.enix.exchanges.service.*;
import fr.enix.exchanges.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ApplicationServiceConfiguration {

    @Bean
    public ExchangeService exchangeService(final KrakenPrivateRepository krakenPrivateRepository) {
        return new ExchangeServiceImpl(krakenPrivateRepository);
    }

    @Bean
    public MarketOfferService marketOfferService(final MarketOfferHistoryRepository marketOfferHistoryRepository) {
        return new MarketOfferServiceImpl(marketOfferHistoryRepository);
    }

    @Bean
    public TradingDecisionService tradingBearingStrategyDecisionServiceImpl(final PriceReferenceService priceReferenceService,
                                                                            final ExchangeService exchangeService,
                                                                            final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository,
                                                                            final AssetOrderIntervalRepository assetOrderIntervalRepository) {
        return
            new TradingBearingStrategyDecisionServiceImpl(
                    priceReferenceService,
                    exchangeService,
                    applicationCurrencyTradingsParameterRepository,
                    assetOrderIntervalRepository
            );
    }

    @Bean
    public PriceReferenceService priceReferenceService(final PriceReferenceRepository priceReferenceRepository) {
        return new PriceReferenceServiceImpl(priceReferenceRepository);
    }

    @Bean
    public TickerService tickerService(final ExchangeService                    exchangeService,
                                       final TradingDecisionService             tradingDecisionService,
                                       final MarketOfferService                 marketOfferService,
                                       final CurrenciesRepresentationService    currenciesRepresentationService,
                                       final TickerMapper tickerMapper,
                                       final PriceReferenceService              priceReferenceService,
                                       final AddOrderMapper addOrderMapper) {
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