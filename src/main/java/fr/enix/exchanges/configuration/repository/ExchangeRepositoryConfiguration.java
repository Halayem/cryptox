package fr.enix.exchanges.configuration.repository;

import fr.enix.exchanges.model.parameters.ApplicationCurrencyTradingsParameter;
import fr.enix.exchanges.model.repository.ApplicationRepositoryProperties;
import fr.enix.exchanges.repository.*;
import fr.enix.exchanges.repository.impl.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties( { ApplicationRepositoryProperties.class, ApplicationCurrencyTradingsParameter.class } )
public class ExchangeRepositoryConfiguration {

    @Bean
    public WebClient exchangeWebClient(final ApplicationRepositoryProperties applicationRepositoryProperties) {
        return
            WebClient
            .builder        ()
            .baseUrl        (applicationRepositoryProperties.getWebservice().getUrl())
            .defaultHeaders (httpHeaders -> {

                httpHeaders.set(HttpHeaders.CONTENT_TYPE,   MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                httpHeaders.set("API-Key",                  applicationRepositoryProperties.getWebservice().getApikey());
            })
            .build          ();
    }

    @Bean
    public MarketOfferHistoryRepository marketOfferHistoryRepository() {
        return new MarketOfferHistoryRepositoryImpl();
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
    public PriceReferenceRepository priceReferenceRepository() { return new PriceReferenceRepositoryImpl(); }

    @Bean
    public HeartbeatRepository heartbeatRepository() {
        return new HeartbeatRepositoryInMemoryImpl();
    }

    @Bean
    public PongRepository pongRepository() { return new PongRepositoryInMemoryImpl(); }

    @Bean
    public ChannelRepository channelRepository() {
        return new ChannelRepositoryInMemoryRepositoryImpl();
    }
}
