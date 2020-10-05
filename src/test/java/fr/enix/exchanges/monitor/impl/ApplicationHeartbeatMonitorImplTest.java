package fr.enix.exchanges.monitor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.manager.HeartbeatManager;
import fr.enix.exchanges.repository.ApplicationMonitoringParametersRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationHeartbeatMonitorImplTest {

    @Autowired private HeartbeatManager heartbeatManager;
    @Autowired private ApplicationHeartbeatMonitorImpl krakenHeartbeatMonitor;
    @Autowired private ApplicationMonitoringParametersRepository applicationMonitoringParametersRepository;

    @Test
    @Order(0)
    void testMonitorHeartbeatWhenNoHeartbeatYetSaved_shouldSetHeartbeatFlagErrorToTrue() {
        krakenHeartbeatMonitor.doVerify();
        assertTrue(krakenHeartbeatMonitor.isError());
    }

    @Test
    @Order(1)
    void testMonitorHeartbeatWhenHeartbeatExceededMaxAge_shouldSetHeartbeatFlagErrorToTrue()
            throws JsonProcessingException, InterruptedException {

        heartbeatManager.managePayload("{\"event\":\"heartbeat\"}");
        Thread.sleep(getThreadDurationSleepToExceedStoredHeartbeatAge());
        krakenHeartbeatMonitor.doVerify();
        assertTrue(krakenHeartbeatMonitor.isError());
    }

    @Test
    @Order(2)
    void testMonitorHeartbeatWhenHeartbeatDoesNotExceededMaxAge_shouldSetHeartbeatFlagErrorToFalse() throws JsonProcessingException {
        heartbeatManager.managePayload("{\"event\":\"heartbeat\"}");
        krakenHeartbeatMonitor.doVerify();
        assertFalse( krakenHeartbeatMonitor.isError() );
    }

    private long getThreadDurationSleepToExceedStoredHeartbeatAge() {
        return ( 2 * TimeUnit
                        .of         (applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("heartbeat").getTimeunit())
                        .toMillis   (applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("heartbeat").getMaxAge())
        );
    }
}
