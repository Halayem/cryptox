package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.OpenOrdersMapper;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import fr.enix.exchanges.service.*;
import fr.enix.exchanges.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ExchangeServiceConfiguration {

    @Bean
    public ExchangeService exchangeService(final KrakenPrivateRepository exchangeRepository,
                                           final AddOrderMapper addOrderMapper,
                                           final OpenOrdersMapper openOrdersMapper) {
        return new ExchangeServiceImpl(exchangeRepository, addOrderMapper, openOrdersMapper);
    }

    @Bean
    public MarketOfferService marketOfferService(final MarketOfferHistoryRepository marketOfferHistoryRepository) {
        return new MarketOfferServiceImpl(marketOfferHistoryRepository);
    }

    @Bean
    public TransactionDecisionService transactionDecisionService() {
        return new TransactionDecisionServiceImpl();
    }

    @Bean
    public CurrenciesRepresentationService currenciesRepresentationService() {
        log.info("kraken currencies representation service bean will be created");
        return new KrakenCurrenciesRepresentationServiceImpl();
    }

    @Bean
    public ApplicationTradingConfigurationService applicationTradingConfigurationService(final ApplicationCurrencyTradingsParameterRepository applicationTradingConfigurationRepository,
                                                                                         final CurrenciesRepresentationService currenciesRepresentationService) {
        log.info("kraken trading configuration service bean will be created");
        return new KrakenTradingConfigurationServiceImpl(applicationTradingConfigurationRepository,
                                                         currenciesRepresentationService);
    }
}
