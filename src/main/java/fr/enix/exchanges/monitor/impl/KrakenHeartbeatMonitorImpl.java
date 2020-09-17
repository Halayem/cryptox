package fr.enix.exchanges.monitor.impl;

import fr.enix.exchanges.model.parameters.KrakenHeartbeatMonitorParameters;
import fr.enix.exchanges.monitor.AbstractApplicationMonitor;
import fr.enix.exchanges.repository.HeartbeatRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class KrakenHeartbeatMonitorImpl extends AbstractApplicationMonitor {

    private final HeartbeatRepository               heartbeatRepository;
    private final KrakenHeartbeatMonitorParameters  krakenHeartbeatMonitorParameters;

    @Getter private boolean error = false;

    @Override
    public void start() {
        startMonitoring(
                krakenHeartbeatMonitorParameters.getFrequency(),
                krakenHeartbeatMonitorParameters.getTimeunit()
        );
        log.info(getHeartbeatConfigurationParametersForLog());
    }

    @Override
    public void doVerify() {
        if      ( heartbeatRepository.getLastHeartbeatDatetime() == null )  { heartbeatNotFoundError();         }
        else if ( isHeartbeatExceededMaxAge() )                             { heartbeatExceededMaxAgeError();   }
        else                                                                { heartbeatOk();                    }
    }

    private boolean isHeartbeatExceededMaxAge() {
        return ( krakenHeartbeatMonitorParameters
                .getTimeunit()
                .between(heartbeatRepository.getLastHeartbeatDatetime(), LocalDateTime.now() ) > krakenHeartbeatMonitorParameters.getMaxAge()
        );
    }

    private void heartbeatNotFoundError() {
        error = true;
        log.error("no heartbeat saved");
    }

    private void heartbeatExceededMaxAgeError() {
        error = true;
        log.error(  String.format( "heartbeat date: %s has exceeded the max age: %d", heartbeatRepository.getLastHeartbeatDatetime(), krakenHeartbeatMonitorParameters.getMaxAge() ) );
    }

    private void heartbeatOk() {
        error = false;
        log.info("heartbeat monitoring OK");
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
