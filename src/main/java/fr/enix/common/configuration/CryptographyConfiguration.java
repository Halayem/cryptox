package fr.enix.common.configuration;

import fr.enix.common.utils.cryptography.MacCryptographyUtils;
import fr.enix.common.utils.cryptography.MessageDigestUtils;
import fr.enix.exchanges.model.repository.ApplicationRepositoryProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
@EnableConfigurationProperties( ApplicationRepositoryProperties.class )
public class CryptographyConfiguration {

    @Bean
    public MacCryptographyUtils macCryptographyUtilsHmacSha512(final ApplicationRepositoryProperties applicationRepositoryProperties)
            throws InvalidKeyException, NoSuchAlgorithmException {

        return new MacCryptographyUtils(
                Base64.getDecoder().decode(applicationRepositoryProperties.getWebservice().getPrivateKey()),
                "HmacSHA512"
        );
    }

    @Bean
    public MessageDigestUtils messageDigestUtilsSha256() throws NoSuchAlgorithmException {
        return new MessageDigestUtils("SHA-256");
    }
}
