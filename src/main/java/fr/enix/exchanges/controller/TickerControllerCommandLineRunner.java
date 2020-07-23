package fr.enix.exchanges.controller;

import fr.enix.exchanges.model.CryptoxControllerProperties;
import fr.enix.exchanges.model.websocket.AssetPair;
import fr.enix.exchanges.model.parameters.XzAsset;
import fr.enix.exchanges.mapper.AssetMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@EnableConfigurationProperties( CryptoxControllerProperties.class )
@Slf4j
@AllArgsConstructor
public class TickerControllerCommandLineRunner implements CommandLineRunner {

    private final Mono<Void> litecoinToEuroTickerWebSocketClient;
    private final CryptoxControllerProperties cryptoxControllerProperties;
    private final AssetMapper assetMapper;

    @Override
    public void run(String... args) throws Exception {
        Mono.just       (cryptoxControllerProperties.getTickerByAssetPair(
                            assetMapper.mapAssetPairForWebSocket(
                                AssetPair.builder   ()
                                         .from      (XzAsset.XLTC)
                                         .to        (XzAsset.ZEUR)
                                         .build     ()
                            )
                        ))
            .filter     (ticker -> ticker.isRun())
            .subscribe  (ticker -> startLitecoinToEuroTickerWebSocketClient(ticker.getBlock()));
    }

    private final Long zero = 0L;
    private void startLitecoinToEuroTickerWebSocketClient(final Long blockDuration) {


        if (zero.equals(blockDuration)) {
            log.info("web socket for litecoin update price will be established in 10 seconds !");
            Executors.newSingleThreadScheduledExecutor().schedule(this::startLitecoinToEuroWebSocketCLientInfinite, 10L, TimeUnit.SECONDS);
        } else {
            startLitecoinToEuroWebSocketCLientWithDuration(blockDuration);
        }
    }

    private void startLitecoinToEuroWebSocketCLientInfinite() {
        log.info("starting ticker litecoin to euro websocket client for infinite...");
        litecoinToEuroTickerWebSocketClient.block();
    }

    private void startLitecoinToEuroWebSocketCLientWithDuration(final Long durationInSeconds) {
        log.info("starting ticker litecoin to euro websocket client for {} seconds...", durationInSeconds);
        try {
            litecoinToEuroTickerWebSocketClient.block(Duration.ofSeconds(durationInSeconds));
        }
        catch (RuntimeException e) {
            log.warn( "it seems that blocking timeout of {} seconds was reached", durationInSeconds );
        }

    }
}
