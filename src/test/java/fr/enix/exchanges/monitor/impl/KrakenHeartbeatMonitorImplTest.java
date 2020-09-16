package fr.enix.exchanges.monitor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.manager.HeartbeatManager;
import fr.enix.exchanges.model.parameters.KrakenHeartbeatMonitorParameters;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class KrakenHeartbeatMonitorImplTest {

    @Autowired private HeartbeatManager heartbeatManager;
    @Autowired private KrakenHeartbeatMonitorImpl krakenHeartbeatMonitor;
    @Autowired private KrakenHeartbeatMonitorParameters krakenHeartbeatMonitorParameters;

    @Test
    void testMonitorHeartbeatWhenNoHeartbeatYetSaved_shouldSetHeartbeatFlagErrorToTrue() {
        krakenHeartbeatMonitor.verifyHeartbeat();
        assertTrue(krakenHeartbeatMonitor.isHeartbeatError());
    }

    @Test
    void testMonitorHeartbeatWhenHeartbeatExceededMaxAge_shouldSetHeartbeatFlagErrorToTrue()
            throws JsonProcessingException, InterruptedException {

        heartbeatManager.managePayload("{\"event\":\"heartbeat\"}");
        Thread.sleep(getThreadDurationSleepToExceedStoredHeartbeatAge());
        krakenHeartbeatMonitor.verifyHeartbeat();
        assertTrue(krakenHeartbeatMonitor.isHeartbeatError());
    }

    @Test
    void testMonitorHeartbeatWhenHeartbeatDoesNotExceededMaxAge_shouldSetHeartbeatFlagErrorToFalse() throws JsonProcessingException {
        heartbeatManager.managePayload("{\"event\":\"heartbeat\"}");
        krakenHeartbeatMonitor.verifyHeartbeat();
        assertFalse( krakenHeartbeatMonitor.isHeartbeatError() );
    }

    private long getThreadDurationSleepToExceedStoredHeartbeatAge() {
        return ( 2 * TimeUnit
                        .of         (krakenHeartbeatMonitorParameters.getTimeunit())
                        .toMillis   (krakenHeartbeatMonitorParameters.getMaxAge())
        );
    }
}
