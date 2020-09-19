package fr.enix.exchanges.monitor.impl;

import fr.enix.exchanges.model.repository.MonitoringConfiguration;
import fr.enix.exchanges.repository.ApplicationMonitoringParametersRepository;
import fr.enix.exchanges.monitor.AbstractApplicationMonitor;
import fr.enix.exchanges.repository.HeartbeatRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ApplicationHeartbeatMonitorImpl extends AbstractApplicationMonitor {

    private final HeartbeatRepository heartbeatRepository;
    private final MonitoringConfiguration monitoringConfiguration;

    public ApplicationHeartbeatMonitorImpl(final HeartbeatRepository heartbeatRepository,
                                           final ApplicationMonitoringParametersRepository applicationMonitoringParametersRepository) {
        this.heartbeatRepository = heartbeatRepository;
        this.monitoringConfiguration = applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("heartbeat");
    }

    @Override
    public void start() {
        startMonitoring(
                monitoringConfiguration.getFrequency(),
                monitoringConfiguration.getTimeunit()
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
        return ( monitoringConfiguration
                .getTimeunit()
                .between(heartbeatRepository.getLastHeartbeatDatetime(), LocalDateTime.now() ) > monitoringConfiguration.getMaxAge()
        );
    }

    @Getter private boolean error = false;

    private void heartbeatNotFoundError() {
        error = true;
        log.error("no heartbeat saved");
    }

    private void heartbeatExceededMaxAgeError() {
        error = true;
        log.error(  String.format( "heartbeat date: %s has exceeded the max age: %d", heartbeatRepository.getLastHeartbeatDatetime(), monitoringConfiguration.getMaxAge() ) );
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
                monitoringConfiguration.getFrequency(), monitoringConfiguration.getTimeunit(),
                monitoringConfiguration.getMaxAge(),    monitoringConfiguration.getTimeunit()
        );
    }
}
