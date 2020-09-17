package fr.enix.exchanges.model.parameters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KrakenPingPropertiesTest {

    @Autowired
    private KrakenPingProperties krakenPingProperties;

    @Test
    void testKrakenPingProperties() {
        assertEquals("{\"event\": \"ping\"}", krakenPingProperties.getPayload());
        assertEquals(60l, krakenPingProperties.getFrequency());
    }
}
