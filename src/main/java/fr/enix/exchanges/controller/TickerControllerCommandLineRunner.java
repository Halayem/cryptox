package fr.enix.exchanges.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@AllArgsConstructor
public class TickerControllerCommandLineRunner implements CommandLineRunner  {

    private final Mono<Void> tickerWebSocketClient;

    @Override
    public void run(String... args) throws Exception {
        startTickerWebSocketClient();
    }

    private void startTickerWebSocketClient() {
        log.info("starting ticker websocket client ...");
        tickerWebSocketClient.block();
    }
}
