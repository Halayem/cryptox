package fr.enix.exchanges.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@AllArgsConstructor
public class TickerControllerCommandLineRunner implements CommandLineRunner {

    private final Mono<Void> tickerWebSocketClient;

    @Override
    public void run(String... args) throws Exception {
        final long delayedDuration  = 10L;
        final TimeUnit timeUnit     = TimeUnit.SECONDS;

        log.info("scheduled single thread running, runnable: startTickerWebSocketClient, delayed: {} {}", delayedDuration, timeUnit);
        Executors.newSingleThreadScheduledExecutor().schedule(this::startTickerWebSocketClient, delayedDuration, timeUnit);
    }

    private void startTickerWebSocketClient() {
        log.info("will establishing a new web socket communication to receive real time market place price update");
        //tickerWebSocketClient.block(Duration.ofSeconds(5L));
        tickerWebSocketClient.block();
    }

}
