package fr.enix.exchanges.model.parameters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KrakenHeartbeatMonitorParametersTest {

    @Autowired
    private KrakenHeartbeatMonitorParameters krakenHeartbeatMonitorParameters;

    @Test
    void testKrakenHeartbeatMonitorParameters() {
        assertEquals(ChronoUnit.SECONDS, krakenHeartbeatMonitorParameters.getTimeunit());
        assertEquals(10l, krakenHeartbeatMonitorParameters.getFrequency());
        assertEquals(2l, krakenHeartbeatMonitorParameters.getMaxAge());
    }
}
