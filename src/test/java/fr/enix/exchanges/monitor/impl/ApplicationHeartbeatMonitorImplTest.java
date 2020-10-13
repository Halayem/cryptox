package fr.enix.exchanges.monitor.impl;

import fr.enix.exchanges.repository.HeartbeatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationHeartbeatMonitorImplTest {

    @Autowired
    private ApplicationHeartbeatMonitorImpl heartbeatMonitor;

    @MockBean
    private HeartbeatRepository heartbeatRepository;

    @Test
    void testMonitorHeartbeatWhenNoHeartbeatYetSaved_shouldSetHeartbeatFlagErrorToTrue() {
        heartbeatMonitor.doVerify();
        assertTrue(heartbeatMonitor.isError());
    }

    @Test
    void testMonitorHeartbeatWhenHeartbeatExceededMaxAge_shouldSetHeartbeatFlagErrorToTrue() {
        final ApplicationHeartbeatMonitorImpl heartbeatMonitorSpy = Mockito.spy(heartbeatMonitor);
        setupHeartbeatMonitorSpy( heartbeatMonitorSpy, true );
        setupHeartbeatRepository( LocalDateTime.now() );

        heartbeatMonitorSpy.doVerify();
        assertTrue(heartbeatMonitorSpy.isError());
    }

    @Test
    void testMonitorHeartbeatWhenHeartbeatDoesNotExceededMaxAge_shouldSetHeartbeatFlagErrorToFalse()  {
        final ApplicationHeartbeatMonitorImpl heartbeatMonitorSpy = Mockito.spy(heartbeatMonitor);
        setupHeartbeatMonitorSpy( heartbeatMonitorSpy, false );
        setupHeartbeatRepository( LocalDateTime.now() );

        heartbeatMonitorSpy.doVerify();
        assertFalse( heartbeatMonitorSpy.isError() );
    }

    private void setupHeartbeatMonitorSpy(final ApplicationHeartbeatMonitorImpl heartbeatMonitorSpy,
                                          final boolean isHeartbeatExceededMaxAge) {
        Mockito
        .doReturn   (isHeartbeatExceededMaxAge)
        .when       (heartbeatMonitorSpy).isHeartbeatExceededMaxAge();
    }

    private void setupHeartbeatRepository(final LocalDateTime lastHeartbeatDateTime) {
        Mockito
        .doReturn   (lastHeartbeatDateTime)
        .when       (heartbeatRepository).getLastHeartbeatDatetime();
    }

}
