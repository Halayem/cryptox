package fr.enix.exchanges.configuration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TickerControllerConfigurationTest {

    @Autowired
    private String tickerSubscriptionMessage;

    @Test
    void testTickerSubscriptionMessage() {
        assertEquals(
        "{" +
                    "\"event\":\"subscribe\","                              +
                    "\"pair\":[\"LTC/EUR\",\"XBT/EUR\",\"XRP/EUR\"]," +
                    "\"subscription\":{\"name\":\"ticker\"}"                +
                "}",
                tickerSubscriptionMessage
        );
    }
}
