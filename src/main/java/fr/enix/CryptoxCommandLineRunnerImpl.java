package fr.enix;

import fr.enix.exchanges.monitor.ApplicationMonitor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
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

    private void startWebSocketClient() {
        Executors
        .newSingleThreadScheduledExecutor()
        .schedule(this::startTickerWebSocketClient, 10l, TimeUnit.SECONDS);
    }

    private void startTickerWebSocketClient() {
        tickerWebSocketClient.block();
    }

    private void startMonitoring() {
        heartbeatMonitor.start();
        pongMonitor.start();
    }

}
