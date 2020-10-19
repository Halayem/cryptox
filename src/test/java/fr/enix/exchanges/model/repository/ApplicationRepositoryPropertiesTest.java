package fr.enix.exchanges.model.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ApplicationRepositoryPropertiesTest {

    @Autowired
    private ApplicationRepositoryProperties applicationRepositoryProperties;
    @Test
    void testApplicationRepositoryProperties() {
        assertEquals(
            "ARhllSp+6hRurZfDbK64sXliA7th+/hGg5aTKXo/MP0Tlb0GikZUtg6K",
            applicationRepositoryProperties.getWebservice().getApikey()
        );
        assertEquals(
            "u6iAS7Wr9h5W9jEOjuNZnhemUBgM0JY4rEkNasKDyg88o53r3hk4W/e5XHdNV6I5CUs1C+N4/kOYr2kdzcmKmg==",
            applicationRepositoryProperties.getWebservice().getPrivatekey()
        );

        assertEquals("http://localhost:8080/",      applicationRepositoryProperties.getWebservice().getUrl());
        assertEquals("wss://beta-ws.kraken.com/",   applicationRepositoryProperties.getWebSocket().getUrl());

        assertEquals("/0/private/AddOrder",     applicationRepositoryProperties.getWebservice().getUrn().get("addOrder"));
        assertEquals("/0/private/Balance",      applicationRepositoryProperties.getWebservice().getUrn().get("balance"));
        assertEquals("/0/private/TradeBalance", applicationRepositoryProperties.getWebservice().getUrn().get("tradeBalance"));
        assertEquals("/0/private/OpenOrders",   applicationRepositoryProperties.getWebservice().getUrn().get("openOrders"));

        assertEquals("{\"event\": \"ping\"}",   applicationRepositoryProperties.getWebSocket().getPing().getPayload());
        assertEquals(20,                        applicationRepositoryProperties.getWebSocket().getPing().getFrequency());
    }
}
