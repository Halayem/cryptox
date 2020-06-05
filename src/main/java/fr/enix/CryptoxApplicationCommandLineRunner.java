package fr.enix;

import fr.enix.exchanges.model.business.AddOrderInput;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.kraken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;

@Component
@Slf4j
@AllArgsConstructor
public class CryptoxApplicationCommandLineRunner implements CommandLineRunner {

    private final ExchangeService exchangeService;

    @Override
    public void run(String... args) throws Exception {
        log.info("ready to run something on startup!");
    }

    private void startWebSocketClient() {
        WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();
        webSocketClient.execute(
            URI.create("wss://ws.kraken.com/"),
            webSocketSession ->
                webSocketSession.send(
                    Mono.just(
                        webSocketSession.textMessage("{ \"event\": \"subscribe\", \"pair\": [\"LTC/EUR\"], \"subscription\": { \"name\": \"ticker\" }}")
                    ))
                .thenMany(webSocketSession.receive  ()
                                          .map      (WebSocketMessage::getPayloadAsText)
                        .doOnNext(payload -> {
                          System.out.println( "new data arrived: " + payload );
                        })
                )
                .then())
                .block();
    }


    private void runAddOrder() {
        exchangeService.addOrder(
            AddOrderInput.builder   ()
                    .assetPair      (AssetPair.LITECOIN_TO_EURO)
                    .addOrderType   (AddOrderType.SELL)
                    .orderType      (OrderType.LIMIT)
                    .price          (new BigDecimal(40  ))
                    .volume         (new BigDecimal(1   ))
                    .build()
        ).subscribe(response -> {
            log.info( "order placed, kraken response: {}", response );
        });
    }

    private void runGetBalance() {
        exchangeService.getBalance().subscribe(response -> {
            if ( response != null && response.getResult() != null ) {
                log.info("balance in litecoin: {}", response.getResult().get( Asset.LITECOIN.getCode() ));
                log.info("balance in euro: {}", response.getResult().get( Asset.EURO.getCode() ));
            }
        });
    }

    private void runGetTradeBalance() {
        exchangeService.getTradeBalance(AssetClass.CURRENCY).subscribe(response -> {
            log.info("trade balance: {}", response);
        });
    }
}
