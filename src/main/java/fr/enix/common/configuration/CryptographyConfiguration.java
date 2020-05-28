package fr.enix.common.configuration;

import fr.enix.common.utils.cryptography.MacCryptographyUtils;
import fr.enix.common.utils.cryptography.MessageDigestUtils;
import fr.enix.exchanges.model.ExchangeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
@EnableConfigurationProperties( ExchangeProperties.class )
public class CryptographyConfiguration {

    @Bean
    public MacCryptographyUtils macCryptographyUtilsHmacSha512(final ExchangeProperties exchangeProperties)
            throws InvalidKeyException, NoSuchAlgorithmException {

        return new MacCryptographyUtils(
                Base64.getDecoder().decode(exchangeProperties.getPrivateKey()),
                "HmacSHA512"
        );
    }

    @Bean
    public MessageDigestUtils messageDigestUtilsSha256() throws NoSuchAlgorithmException {
        return new MessageDigestUtils("SHA-256");
    }
}
