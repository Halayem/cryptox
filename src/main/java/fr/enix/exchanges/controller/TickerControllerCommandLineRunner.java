package fr.enix.exchanges.controller;

import fr.enix.exchanges.model.CryptoxControllerProperties;
import fr.enix.kraken.AssetPair;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@EnableConfigurationProperties( CryptoxControllerProperties.class )
@Slf4j
@AllArgsConstructor
public class TickerControllerCommandLineRunner implements CommandLineRunner {

    private final Mono<Void> litecoinToEuroTickerWebSocketClient;
    private final CryptoxControllerProperties cryptoxControllerProperties;

    @Override
    public void run(String... args) throws Exception {
        Mono.just       (cryptoxControllerProperties.getTickerByAssetPair(AssetPair.LITECOIN_TO_EURO))
            .filter     (ticker -> ticker.isRun())
            .subscribe  (ticker -> startLitecoinToEuroTickerWebSocketClient(ticker.getBlock()));
    }

    private final Long zero = 0L;
    private void startLitecoinToEuroTickerWebSocketClient(final Long blockDuration) {
        log.info("starting ticker litecoin_to_euro websocket client...");
        if (zero.equals(blockDuration)) {
            litecoinToEuroTickerWebSocketClient.block();
        } else {
            litecoinToEuroTickerWebSocketClient.block(Duration.ofSeconds(blockDuration));
        }

    }
}
