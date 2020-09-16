package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.model.parameters.KrakenHeartbeatMonitorParameters;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class HeartbeatManagerTest {

    @Autowired
    private HeartbeatManager heartbeatManager;

    @Autowired
    private KrakenHeartbeatMonitorParameters krakenHeartbeatMonitorParameters;

    @Test
    void testMonitorHeartbeatWhenNoHeartbeatYetSaved_shouldThrowRuntimeException() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                heartbeatManager.monitorHeartbeat()
        );
        assertEquals( "no heartbeat saved", exception.getMessage() );
    }

    @Test
    void testMonitorHeartbeatWhenHeartbeatExceededMaxAge_shouldThrowRuntimeException() throws JsonProcessingException, InterruptedException {
        heartbeatManager.managePayload("{\"event\":\"heartbeat\"}");
        Thread.sleep(getThreadDurationSleepToExceedStoredHeartbeatAge());
        Exception exception = assertThrows(RuntimeException.class, () ->
                heartbeatManager.monitorHeartbeat()
        );
        assertTrue(  exception.getMessage().startsWith("heartbeat date: ") );
    }

    @Test
    void testMonitorHeartbeatWhenHeartbeatExceededMaxAge_shouldBeOK() throws JsonProcessingException {
        heartbeatManager.managePayload("{\"event\":\"heartbeat\"}");
        assertDoesNotThrow( () -> heartbeatManager.monitorHeartbeat() );
    }

    private long getThreadDurationSleepToExceedStoredHeartbeatAge() {
        return ( 2 * TimeUnit
                        .of         (krakenHeartbeatMonitorParameters.getTimeunit())
                        .toMillis   (krakenHeartbeatMonitorParameters.getMaxAge())
        );
    }
}
