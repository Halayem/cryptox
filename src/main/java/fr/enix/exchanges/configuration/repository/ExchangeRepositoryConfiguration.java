package fr.enix.exchanges.configuration.repository;

import fr.enix.common.service.KrakenRepositoryService;
import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.model.ExchangeProperties;
import fr.enix.exchanges.model.parameters.ApplicationCurrencyTradingsParameter;
import fr.enix.exchanges.repository.*;
import fr.enix.exchanges.repository.impl.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties( { ExchangeProperties.class, ApplicationCurrencyTradingsParameter.class } )
public class ExchangeRepositoryConfiguration {

    @Bean
    public WebClient krakenPrivateWebClient(final ExchangeProperties exchangeProperties) {
        return WebClient.builder        ()
                        .baseUrl        (exchangeProperties.getUrl().get("webservice"))
                        .defaultHeaders (httpHeaders -> {
                            httpHeaders.set(HttpHeaders.CONTENT_TYPE,   MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                            httpHeaders.set("API-Key",                  exchangeProperties.getApiKey());
                        })
                        .build          ();
    }

    @Bean
    public KrakenPrivateRepository krakenPrivateRepository(final WebClient krakenPrivateWebClient,
                                                           final KrakenRepositoryService krakenService,
                                                           final AddOrderMapper addOrderMapper) {
        return new KrakenPrivateRepositoryImpl(krakenPrivateWebClient, krakenService, addOrderMapper);
    }

    @Bean
    public MarketOfferHistoryRepository marketOfferHistoryRepository() {
        return new MarketOfferHistoryRepositoryImpl();
    }

    @Bean
    public AssetOrderIntervalRepository assetOrderIntervalRepository() {
        return new AssetOrderIntervalRepositoryKrakenImpl();
    }

    @Bean
    public ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository(final ApplicationCurrencyTradingsParameter applicationCurrencyTradingsParameter) {
        return new ApplicationCurrencyTradingsParameterRepositoryImpl(applicationCurrencyTradingsParameter);
    }

    @Bean
    public ApplicationThresholdStrategyParameterRepository applicationThresholdStrategyParameterRepository(final ApplicationCurrencyTradingsParameter applicationCurrencyTradingsParameter) {
        return new ApplicationThresholdStrategyParameterRepositoryImpl(applicationCurrencyTradingsParameter);
    }

    @Bean
    public ApplicationBearingStrategyParameterRepository applicationBearingStrategyParameterRepository(final ApplicationCurrencyTradingsParameter applicationCurrencyTradingsParameter) {
        return new ApplicationCurrencyBearingStrategyTradingsParameterRepositoryImpl(applicationCurrencyTradingsParameter);
    }

    @Bean
    public HeartbeatRepository heartbeatRepository() {
        return new HeartbeatRepositoryInMemoryImpl();
    }

    @Bean
    public PongRepository pongRepository() { return new KrakenPongRepositoryInMemoryImpl(); }
}
