package fr.enix.exchanges.configuration.adaptor;

import fr.enix.exchanges.adaptor.ApplicationAssetPairTickerDecisionAdaptor;
import fr.enix.exchanges.adaptor.kraken.KrakenAssetPairTickerDecisionAdaptorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("kraken")
public class KrakenAdaptorConfiguration {

    @Bean
    public ApplicationAssetPairTickerDecisionAdaptor applicationAssetPairTickerDecisionAdaptor() {
        return new KrakenAssetPairTickerDecisionAdaptorImpl();
    }
}
