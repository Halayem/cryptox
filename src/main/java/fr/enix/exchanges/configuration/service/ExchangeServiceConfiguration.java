package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.impl.ExchangeServiceImpl;
import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.OpenOrdersMapper;
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

}
