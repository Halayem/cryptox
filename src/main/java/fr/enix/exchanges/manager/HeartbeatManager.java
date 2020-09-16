package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.model.parameters.KrakenHeartbeatMonitorParameters;
import fr.enix.exchanges.repository.HeartbeatRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class HeartbeatManager implements WebSocketSubscriptionManager {

    private final HeartbeatRepository heartbeatRepository;
    private final KrakenHeartbeatMonitorParameters krakenHeartbeatMonitorParameters;

    @Override
    public void managePayload(String payload) throws JsonProcessingException {
        heartbeatRepository.saveHeartbeatDatetime();
        log.info("received heartbeat payload: {}", payload);
    }

    @PostConstruct
    public void init() {
        log.info(getHeartbeatConfigurationParametersForLog());

        Executors
                .newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(
                        this::monitorHeartbeat,
                        1,
                        krakenHeartbeatMonitorParameters.getFrequency(),
                        TimeUnit.of(krakenHeartbeatMonitorParameters.getTimeunit())
                );
    }

    protected void monitorHeartbeat() {

        if ( heartbeatRepository.getLastHeartbeatDatetime() == null ) {
            throw new RuntimeException( "no heartbeat saved");
        } else if ( isHeartbeatExceededMaxAge() ) {
            throw new RuntimeException(
                    String.format(
                        "heartbeat date: %s has exceeded the max age: %d",
                        heartbeatRepository.getLastHeartbeatDatetime(),
                        krakenHeartbeatMonitorParameters.getMaxAge()
                    )
            );
        }
        log.info("heartbeat monitoring OK");
    }

    private boolean isHeartbeatExceededMaxAge() {
        return ( krakenHeartbeatMonitorParameters
                .getTimeunit()
                .between(heartbeatRepository.getLastHeartbeatDatetime(), LocalDateTime.now() ) > krakenHeartbeatMonitorParameters.getMaxAge()
        );

    }

    private String getHeartbeatConfigurationParametersForLog() {
        return String.format(
            "heartbeat monitoring configuration:" +
            "\n\t - time unit --- %s" +
            "\n\t - frequency --- %d" +
            "\n\t - max age ----- %d",
            krakenHeartbeatMonitorParameters.getTimeunit(),
            krakenHeartbeatMonitorParameters.getFrequency(),
            krakenHeartbeatMonitorParameters.getMaxAge()
        );
    }

}
