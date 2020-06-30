package fr.enix.exchanges.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties =
        {
            "cryptox.controller.tickers[0].run=true",
            "cryptox.controller.tickers[0].assetPair=XLTC/ZEUR",
            "cryptox.controller.tickers[0].block=5"
        })
public class TickerControllerCommandLineRunnerTest {

    @Test
    public void testRun() {}
}
