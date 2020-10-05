package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.repository.*;
import fr.enix.exchanges.service.*;
import fr.enix.exchanges.service.impl.*;
import fr.enix.exchanges.service.impl.kraken.KrakenChannelServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ExchangeServiceConfiguration {

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
                                                                            final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository) {
        return
            new TradingBearingStrategyDecisionServiceImpl(priceReferenceService,
                                                          exchangeService,
                                                          applicationCurrencyTradingsParameterRepository
            );
    }

    @Bean
    public CurrenciesRepresentationService currenciesRepresentationService() {
        log.info("kraken currencies representation service bean will be created");
        return new KrakenCurrenciesRepresentationServiceImpl();
    }

    @Bean
    public ChannelService channelService(final ChannelRepository channelRepository,
                                         final CurrenciesRepresentationService currenciesRepresentationService) {
        return new KrakenChannelServiceImpl(channelRepository, currenciesRepresentationService);
    }

    @Bean
    public PriceReferenceService priceReferenceService(final PriceReferenceRepository priceReferenceRepository) {
        return new PriceReferenceServiceImpl(priceReferenceRepository);
    }

    @Bean
    public ApplicationTradingConfigurationService applicationTradingConfigurationService(final CurrenciesRepresentationService currenciesRepresentationService,
                                                                                         final ApplicationCurrencyTradingsParameterRepository applicationTradingConfigurationRepository) {
        log.info("kraken trading configuration service bean will be created");
        return new KrakenTradingConfigurationServiceImpl(currenciesRepresentationService, applicationTradingConfigurationRepository);
    }
}
