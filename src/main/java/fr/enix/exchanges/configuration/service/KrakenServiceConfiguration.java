package fr.enix.exchanges.configuration.service;

import fr.enix.common.MarketPlace;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.ChannelRepository;
import fr.enix.exchanges.service.ApplicationTradingConfigurationService;
import fr.enix.exchanges.service.ChannelService;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import fr.enix.exchanges.service.MarketPlaceService;
import fr.enix.exchanges.service.impl.MarketPlaceServiceImpl;
import fr.enix.exchanges.service.impl.kraken.KrakenCurrenciesRepresentationServiceImpl;
import fr.enix.exchanges.service.impl.kraken.KrakenTradingConfigurationServiceImpl;
import fr.enix.exchanges.service.impl.kraken.KrakenChannelServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("kraken")
public class KrakenServiceConfiguration {

    @Bean
    public CurrenciesRepresentationService currenciesRepresentationService() {
        return new KrakenCurrenciesRepresentationServiceImpl();
    }

    @Bean
    public ChannelService channelService(final ChannelRepository channelRepository,
                                         final CurrenciesRepresentationService currenciesRepresentationService) {
        return new KrakenChannelServiceImpl(channelRepository, currenciesRepresentationService);
    }

    @Bean
    public ApplicationTradingConfigurationService applicationTradingConfigurationService(final CurrenciesRepresentationService currenciesRepresentationService,
                                                                                         final ApplicationCurrencyTradingsParameterRepository applicationTradingConfigurationRepository) {
        return new KrakenTradingConfigurationServiceImpl(currenciesRepresentationService, applicationTradingConfigurationRepository);
    }

    @Bean
    public MarketPlaceService marketPlace() {
        return new MarketPlaceServiceImpl(MarketPlace.KRAKEN);
    }
}
