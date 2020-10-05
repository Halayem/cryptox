package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.MonitoringConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ApplicationMonitoringParametersRepositoryTest {

    @Autowired
    private ApplicationMonitoringParametersRepository applicationMonitoringParametersRepository;

    @Test
    void testApplicationMonitoringHeartbeatMonitorParameters() {
        final MonitoringConfiguration monitoringConfiguration = applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("heartbeat");

        assertEquals(ChronoUnit.SECONDS, monitoringConfiguration.getTimeunit());
        assertEquals(7200l,     monitoringConfiguration.getFrequency());
        assertEquals(1l,        monitoringConfiguration.getMaxAge());
    }

    @Test
    void testApplicationMonitoringPongParameters() {
        final MonitoringConfiguration monitoringConfiguration = applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("pong");

        assertEquals(ChronoUnit.SECONDS, monitoringConfiguration.getTimeunit());
        assertEquals(120l,     monitoringConfiguration.getFrequency());
        assertEquals(1l,        monitoringConfiguration.getMaxAge());
    }

    @Test
    void testApplicationMonitoringForUnknownEvent_shouldThrowRuntimeException() {
        final Exception exception = assertThrows(
                RuntimeException.class,
                () -> applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("test-unknown-event")
        );

        assertEquals("unknown event: test-unknown-event", exception.getMessage());

    }
}
