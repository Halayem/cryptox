package fr.enix.exchanges.configuration.repository;

import fr.enix.common.service.KrakenRepositoryService;
import fr.enix.exchanges.model.ExchangeProperties;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.repository.impl.KrakenPrivateRepositoryImpl;
import fr.enix.mapper.AddOrderMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties( ExchangeProperties.class )
public class ExchangeRepositoryConfiguration {

    @Bean
    public WebClient krakenPrivateWebClient(final ExchangeProperties exchangeProperties) {
        return WebClient.builder        ()
                        .baseUrl        (exchangeProperties.getUrl())
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

}
