package fr.enix.exchanges.controller;

import fr.enix.exchanges.mapper.AssetMapper;
import fr.enix.exchanges.service.ApplicationTradingConfigurationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@AllArgsConstructor
public class TickerControllerCommandLineRunner implements CommandLineRunner {

    private final Mono<Void> litecoinToEuroTickerWebSocketClient;
    private final ApplicationTradingConfigurationService applicationTradingConfigurationService;
    private final AssetMapper assetMapper;

    @Override
    public void run(String... args) throws Exception {
        applicationTradingConfigurationService
        .getAssetPairsToSubscribe()
        .subscribe      (assetPair -> startLitecoinToEuroWebSocketCLientInfinite(assetPair));
    }

    private void startLitecoinToEuroWebSocketCLientInfinite(final String assetPair) {
        log.info("will establishing a new web socket communication to receive real time market place price update for asset pair: {}", assetPair);
        log.warn("web socket communication is hard coded disabled ");
        /*
        try {
            litecoinToEuroTickerWebSocketClient.block();
        }
        catch (RuntimeException e) {
            log.error( "web socket was not created or was interrupted for this asset pair: {}, error message: {} ", assetPair, e.getMessage() );
        }
        */
    }
}
