package fr.enix.exchanges.model.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties( prefix = "application.repository" )
@Getter
@Setter
public class ApplicationRepositoryProperties {

    private Webservice webservice;
    private WebSocket webSocket;

    @Getter
    @Setter
    public static class Webservice {
        private String url;
        private String apikey;
        private String privatekey;
        private Map<String, String> urn;
    }

    @Getter
    @Setter
    public static class WebSocket {
        private String url;
        private Ping ping;

        @Getter
        @Setter
        public static class Ping {
            private String payload;
            private int frequency;
        }
    }

}
