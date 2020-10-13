package fr.enix.exchanges.configuration.repository;

import fr.enix.common.service.EncryptionService;
import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.model.repository.ApplicationRepositoryProperties;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.repository.ExchangeRepository;
import fr.enix.exchanges.repository.impl.kraken.KrakenAssetOrderIntervalRepositoryImpl;
import fr.enix.exchanges.repository.impl.kraken.KrakenExchangeRepositoryImpl;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile("kraken")
@EnableConfigurationProperties( ApplicationRepositoryProperties.class )
public class KrakenExchangeRepositoryConfiguration {

    @Bean
    public ExchangeRepository exchangeRepository(final WebClient exchangeWebClient,
                                                 final EncryptionService encryptionService,
                                                 final AddOrderMapper addOrderMapper,
                                                 final CurrenciesRepresentationService currenciesRepresentationService,
                                                 final ApplicationRepositoryProperties applicationRepositoryProperties) {
        return new KrakenExchangeRepositoryImpl(
                        exchangeWebClient,
                        encryptionService,
                        addOrderMapper,
                        currenciesRepresentationService,
                        applicationRepositoryProperties
        );
    }

    @Bean
    public AssetOrderIntervalRepository assetOrderIntervalRepository() {
        return new KrakenAssetOrderIntervalRepositoryImpl();
    }
}
