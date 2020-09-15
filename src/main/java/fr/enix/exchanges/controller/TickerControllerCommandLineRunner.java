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
        log.info("will establishing a new web socket communication to receive real time market place price update ");
        Executors.newSingleThreadScheduledExecutor().schedule(this::startTickerWebSocketClient, 10L, TimeUnit.SECONDS);
    }

    private void startTickerWebSocketClient() {
        tickerWebSocketClient.block(Duration.ofSeconds(5));
    }

}
