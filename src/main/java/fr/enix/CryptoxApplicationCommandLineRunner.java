package fr.enix;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.model.parameters.OrderType;
import fr.enix.exchanges.model.parameters.XzAsset;
import fr.enix.exchanges.model.websocket.AssetPair;
import fr.enix.exchanges.service.ExchangeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@AllArgsConstructor
public class CryptoxApplicationCommandLineRunner implements CommandLineRunner {

    private final ExchangeService exchangeService;

    @Override
    public void run(String... args) throws Exception {
        log.info("ready to run something on startup!");
    }

    private void runAddOrder() {
        exchangeService.addOrder(
            AddOrderInput.builder   ()
                    .assetPair      (AssetPair.builder  ()
                                              .from     (XzAsset.XLTC)
                                              .to       (XzAsset.ZEUR)
                                              .build    ()
                                    )
                    .addOrderType   (AddOrderType.SELL)
                    .orderType      (OrderType.LIMIT)
                    .price          (new BigDecimal(40  ))
                    .volume         (new BigDecimal(1   ))
                    .build()
        ).subscribe(addOrderOutput -> {
            log.info( "order placed, kraken response: {}", addOrderOutput );
        });
    }

    private void runGetBalance() {
        exchangeService.getBalance().subscribe(response -> {
            if ( response != null && response.getResult() != null ) {
                log.info("balance in litecoin: {}", response.getResult().get( XzAsset.XLTC  ));
                log.info("balance in euro: {}",     response.getResult().get( XzAsset.ZEUR ));
            }
        });
    }

    private void runGetTradeBalance() {
        exchangeService.getTradeBalance(AssetClass.CURRENCY).subscribe(response -> {
            log.info("trade balance: {}", response);
        });
    }

    private void runGetOpenOrders() {
        exchangeService.getOpenOrders().subscribe(response -> {
            log.info("open orders: {}", response);
        });
    }
}
