package fr.enix;

import fr.enix.exchanges.event.WebSocketClientConnectionTerminatedEvent;
import fr.enix.exchanges.monitor.ApplicationMonitor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@AllArgsConstructor
public class CryptoxCommandLineRunnerImpl implements CommandLineRunner {

    private final Mono<Void>            tickerWebSocketClient;
    private final ApplicationMonitor    heartbeatMonitor;
    private final ApplicationMonitor    pongMonitor;

    @Override
    public void run(String... args)  {
        startWebSocketClient();
        startMonitoring();
    }

    @EventListener(WebSocketClientConnectionTerminatedEvent.class)
    public void onWebSocketClientConnectionTerminatedEvent(WebSocketClientConnectionTerminatedEvent webSocketClientConnectionTerminatedEvent) {
        startTickerWebSocketClient();
        log.info("received event: {}, message: {}, trying to reconnect...", webSocketClientConnectionTerminatedEvent.getClass().getName(), webSocketClientConnectionTerminatedEvent.getMessage() );
    }

    private final long startTickerWebSocketClientDelayInSeconds = 10l;

    private void startWebSocketClient() {
        Executors.newSingleThreadScheduledExecutor().schedule( this::startTickerWebSocketClient, startTickerWebSocketClientDelayInSeconds, TimeUnit.SECONDS );
        log.info("starting web socket client in: {} {} ...", startTickerWebSocketClientDelayInSeconds, TimeUnit.SECONDS.toString() );
    }

    private void startTickerWebSocketClient() {
        tickerWebSocketClient.subscribe();
    }

    private void startMonitoring() {
        heartbeatMonitor.start();
        pongMonitor.start();
    }

}
