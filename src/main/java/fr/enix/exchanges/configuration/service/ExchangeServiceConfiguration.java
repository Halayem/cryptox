package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.OpenOrdersMapper;
import fr.enix.exchanges.mapper.ThresholdMapper;
import fr.enix.exchanges.repository.FixedThresholdRepository;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.repository.MarketOfferHistoryRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.FixedThresholdService;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.TransactionDecisionService;
import fr.enix.exchanges.service.impl.ExchangeServiceImpl;
import fr.enix.exchanges.service.impl.FixedThresholdServiceImpl;
import fr.enix.exchanges.service.impl.MarketOfferServiceImpl;
import fr.enix.exchanges.service.impl.TransactionDecisionTradeFixedThresholdServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
        return new TransactionDecisionTradeFixedThresholdServiceImpl();
    }

    @Bean
    public FixedThresholdService fixedThresholdService(final FixedThresholdRepository fixedThresholdRepository,
                                                       final ThresholdMapper thresholdMapper) {
        return new FixedThresholdServiceImpl(fixedThresholdRepository, thresholdMapper);
    }
}
