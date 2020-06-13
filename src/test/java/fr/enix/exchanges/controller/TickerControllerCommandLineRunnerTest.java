package fr.enix.exchanges.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties =
        {
            "cryptox.controller.tickers[0].run=false",
            "cryptox.controller.tickers[0].assetPair=LITECOIN_TO_EURO",
            "cryptox.controller.tickers[0].block=5"
        })
public class TickerControllerCommandLineRunnerTest {

    @Test
    public void testRun() {}
}
