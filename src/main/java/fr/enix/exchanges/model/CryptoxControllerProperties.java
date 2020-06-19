package fr.enix.exchanges.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties( prefix = "cryptox.controller" )
@Getter
@Setter
public class CryptoxControllerProperties {

    private List<Ticker> tickers;

    public Ticker getTickerByAssetPair(final String assetPair) {
        return tickers.stream   ()
                      .filter   (ticker -> ticker.getAssetPair().equals(assetPair))
                      .findFirst()
                      .orElse   (null);
    }

    @Getter
    @Setter
    @ToString
    public static class Ticker {

        private String assetPair;
        private boolean run;
        private Long block;
    }
}
