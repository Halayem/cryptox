package fr.enix.exchanges.configuration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TickerControllerConfigurationTest {

    @Autowired
    private String tickerSubscriptionMessage;

    @Test
    public void testTickerSubscriptionMessage() {
        assertEquals(
        "{" +
                    "\"event\":\"subscribe\","                              +
                    "\"pair\":[\"XLTC/ZEUR\",\"XXBT/ZEUR\",\"XXRP/ZEUR\"]," +
                    "\"subscription\":{\"name\":\"ticker\"}"                +
                "}",
                tickerSubscriptionMessage
        );
    }
}
