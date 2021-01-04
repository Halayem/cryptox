package fr.enix;

import fr.enix.exchanges.event.WebSocketClientConnectionTerminatedEvent;
import fr.enix.exchanges.model.repository.ApplicationRepositoryProperties;
import fr.enix.exchanges.model.repository.Ticker;
import fr.enix.exchanges.monitor.ApplicationMonitor;
import fr.enix.exchanges.repository.TickerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.Disposable;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@EnableConfigurationProperties(ApplicationRepositoryProperties.class)
public class CryptoxCommandLineRunnerImpl implements CommandLineRunner {

    private final TickerWebSocketClientManager tickerWebSocketClientManager;
    private final ApplicationMonitor heartbeatMonitor;
    private final ApplicationMonitor pongMonitor;
    private final TickerRepository tickerRepository;

    public CryptoxCommandLineRunnerImpl(final ApplicationRepositoryProperties applicationRepositoryProperties,
                                        final WebSocketHandler      webSocketHandler,
                                        final ApplicationMonitor    heartbeatMonitor,
                                        final ApplicationMonitor    pongMonitor,
                                        final TickerRepository tickerRepository) {

        this.tickerWebSocketClientManager   = new TickerWebSocketClientManager( URI.create( applicationRepositoryProperties.getWebSocket().getUrl() ), webSocketHandler );
        this.heartbeatMonitor               = heartbeatMonitor;
        this.pongMonitor                    = pongMonitor;
        this.tickerRepository               = tickerRepository;
    }

    @Override
    public void run(String... args)  {
        testSaveTicker();
    }

    public void testSaveTicker() {
        tickerRepository.save(
            Ticker
            .builder()
            .market("kraken")
            .assetPair("litecoin-euro")
            .price(new BigDecimal("18467.54679"))
            .at(LocalDateTime.now())
            .build()
        ).subscribe(ticker -> log.info("ticker saved in database, response: {}", ticker));
    }

    @EventListener(WebSocketClientConnectionTerminatedEvent.class)
    public void onWebSocketClientConnectionTerminatedEvent(WebSocketClientConnectionTerminatedEvent webSocketClientConnectionTerminatedEvent) {
        log.warn("received event: {}, message: {}, trying to reconnect...", webSocketClientConnectionTerminatedEvent.getClass().getName(), webSocketClientConnectionTerminatedEvent.getMessage() );
        tickerWebSocketClientManager.restartTickerWebSocketClient();
    }

    private final long startTickerWebSocketClientDelayInSeconds = 10L;

    private void startWebSocketClient() {
        log.info("starting web socket client in: {} {} ...", startTickerWebSocketClientDelayInSeconds, TimeUnit.SECONDS.toString() );
        Executors.newSingleThreadScheduledExecutor().schedule( tickerWebSocketClientManager::startTickerWebSocketClient, startTickerWebSocketClientDelayInSeconds, TimeUnit.SECONDS );
    }

    private void startMonitoring() {
        heartbeatMonitor.start();
        pongMonitor.start();
    }

    private static class TickerWebSocketClientManager {

        private final URI webSocketUri;
        private final WebSocketHandler  webSocketHandler;

        protected TickerWebSocketClientManager(final URI webSocketUri, final WebSocketHandler  webSocketHandler) {
            this.webSocketHandler   = webSocketHandler;
            this.webSocketUri       = webSocketUri;
        }

        protected void restartTickerWebSocketClient() {
            log.info("restarting ticker web socket client ...");
            stopTickerWebSocketClient();
            startTickerWebSocketClient();
        }

        private Disposable subscription;
        protected void startTickerWebSocketClient() {
            log.info("starting ticker web socket client ...");
            subscription =  new ReactorNettyWebSocketClient().execute   ( webSocketUri, webSocketHandler )
                                                             .subscribe ();
        }

        private void stopTickerWebSocketClient() {
            log.info("stopping ticker web socket client ...");
            if ( subscription == null || subscription.isDisposed() ) {
                log.error("FATAL! can not stop ticker web socket client since subscription <{}> is null or already disposed", subscription);
            } else {
                subscription.dispose();
                subscription = null;
            }
        }
    }

}
