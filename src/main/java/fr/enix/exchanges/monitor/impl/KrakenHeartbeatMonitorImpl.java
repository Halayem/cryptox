package fr.enix.exchanges.monitor.impl;

import fr.enix.exchanges.model.parameters.KrakenHeartbeatMonitorParameters;
import fr.enix.exchanges.monitor.HeartbeatMonitor;
import fr.enix.exchanges.repository.HeartbeatRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class KrakenHeartbeatMonitorImpl implements HeartbeatMonitor {

    private final HeartbeatRepository heartbeatRepository;
    private final KrakenHeartbeatMonitorParameters krakenHeartbeatMonitorParameters;
    @Getter private boolean heartbeatError = false;

    @Override
    public void start() {
        Executors
                .newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(
                        () -> verifyHeartbeat(),
                        1,
                        krakenHeartbeatMonitorParameters.getFrequency(),
                        TimeUnit.of(krakenHeartbeatMonitorParameters.getTimeunit())
                );
    }

    @Override
    public void stop() {

    }

    protected void verifyHeartbeat() {
        if ( heartbeatRepository.getLastHeartbeatDatetime() == null ) {
            heartbeatError = true;
            log.error("no heartbeat saved");
        } else if ( isHeartbeatExceededMaxAge() ) {
            heartbeatError = true;
            log.error(
                    String.format(
                            "heartbeat date: %s has exceeded the max age: %d",
                            heartbeatRepository.getLastHeartbeatDatetime(),
                            krakenHeartbeatMonitorParameters.getMaxAge()
                    )
            );
        } else {
            heartbeatError = false;
            log.info("heartbeat monitoring OK");
        }
    }

    private boolean isHeartbeatExceededMaxAge() {
        return ( krakenHeartbeatMonitorParameters
                .getTimeunit()
                .between(heartbeatRepository.getLastHeartbeatDatetime(), LocalDateTime.now() ) > krakenHeartbeatMonitorParameters.getMaxAge()
        );

    }

    private String getHeartbeatConfigurationParametersForLog() {
        return String.format(
                "heartbeat monitoring configuration:"       +
                "\n\t - check frequency --- every %d %s"    +
                "\n\t - max age ----------- %d %s",
                krakenHeartbeatMonitorParameters.getFrequency(),
                krakenHeartbeatMonitorParameters.getTimeunit(),
                krakenHeartbeatMonitorParameters.getMaxAge(),
                krakenHeartbeatMonitorParameters.getTimeunit()
        );
    }
}
