package fr.enix.common.configuration;

import fr.enix.common.service.KrakenRepositoryService;
import fr.enix.common.service.impl.KrakenRepositoryServiceImpl;
import fr.enix.common.utils.cryptography.MacCryptographyUtils;
import fr.enix.common.utils.cryptography.MessageDigestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KrakenServiceConfiguration {

    @Bean
    public KrakenRepositoryService krakenService(final MessageDigestUtils messageDigestUtilsSha256,
                                                 final MacCryptographyUtils macCryptographyUtilsHmacSha512) {
        return new KrakenRepositoryServiceImpl(messageDigestUtilsSha256, macCryptographyUtilsHmacSha512);
    }
}
