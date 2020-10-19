package fr.enix.common.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

@TestConfiguration
public class CommonApplicationTestConfiguration {

    @Bean
    public String httpHeadersForTest() {
        return "Basic " + Base64Utils.encodeToString( ( "userForTest:userForTest" ).getBytes(StandardCharsets.UTF_8) );
    }
}
