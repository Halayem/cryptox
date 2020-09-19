package fr.enix.exchanges.monitor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.manager.PongManager;
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
class ApplicationPongMonitorImplTest {

    @Autowired
    private PongManager pongManager;
    @Autowired private ApplicationPongMonitorImpl applicationPongMonitor;
    @Autowired private ApplicationMonitoringParametersRepository applicationMonitoringParametersRepository;

    @Test
    @Order(0)
    void testMonitorPongWhenNoPongYetSaved_shouldSetPongFlagErrorToTrue() {
        applicationPongMonitor.doVerify();
        assertTrue(applicationPongMonitor.isError());
    }

    @Test
    @Order(1)
    void testMonitorPongWhenPongExceededMaxAge_shouldSetPongFlagErrorToTrue()
            throws JsonProcessingException, InterruptedException {

        pongManager.managePayload("{\"event\":\"ping\"}");
        Thread.sleep(getThreadDurationSleepToExceedStoredPongAge());
        applicationPongMonitor.doVerify();
        assertTrue(applicationPongMonitor.isError());
    }

    @Test
    @Order(2)
    void testMonitorPongWhenPongDoesNotExceededMaxAge_shouldSetPongFlagErrorToFalse() throws JsonProcessingException {
        pongManager.managePayload("{\"event\":\"ping\"}");
        applicationPongMonitor.doVerify();
        assertFalse( applicationPongMonitor.isError() );
    }

    private long getThreadDurationSleepToExceedStoredPongAge() {
        return ( 2 * TimeUnit
                .of         (applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("pong").getTimeunit())
                .toMillis   (applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("pong").getMaxAge())
        );
    }
}
