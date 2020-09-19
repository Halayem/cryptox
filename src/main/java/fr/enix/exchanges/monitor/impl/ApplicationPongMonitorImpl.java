package fr.enix.exchanges.monitor.impl;

import fr.enix.exchanges.model.repository.MonitoringConfiguration;
import fr.enix.exchanges.monitor.AbstractApplicationMonitor;
import fr.enix.exchanges.repository.ApplicationMonitoringParametersRepository;
import fr.enix.exchanges.repository.PongRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ApplicationPongMonitorImpl extends AbstractApplicationMonitor {

    private final PongRepository pongRepository;
    private final MonitoringConfiguration monitoringConfiguration;

    public ApplicationPongMonitorImpl(final PongRepository pongRepository,
                                      final ApplicationMonitoringParametersRepository applicationMonitoringParametersRepository) {
        this.pongRepository = pongRepository;
        this.monitoringConfiguration = applicationMonitoringParametersRepository.getMonitoringConfigurationForEvent("pong");
    }

    @Override
    public void doVerify() {
        if      ( pongRepository.getLastPongDatetime() == null )    { pongNotFoundError();         }
        else if ( isPongExceededMaxAge() )                          { pongExceededMaxAgeError();   }
        else                                                        { pongOk();                    }
    }

    private boolean isPongExceededMaxAge() {
        return ( monitoringConfiguration
                .getTimeunit()
                .between(pongRepository.getLastPongDatetime(), LocalDateTime.now() ) > monitoringConfiguration.getMaxAge()
        );
    }

    @Getter
    private boolean error = false;

    private void pongNotFoundError() {
        error = true;
        log.error("no pong saved");
    }

    private void pongExceededMaxAgeError() {
        error = true;
        log.error(  String.format( "pong date: %s has exceeded the max age: %d", pongRepository.getLastPongDatetime(), monitoringConfiguration.getMaxAge() ) );
    }

    private void pongOk() {
        error = false;
        log.info("pong monitoring OK");
    }

    @Override
    public void start() {
        startMonitoring(
                monitoringConfiguration.getFrequency(),
                monitoringConfiguration.getTimeunit()
        );
        log.info(getPongConfigurationParametersForLog());
    }

    private String getPongConfigurationParametersForLog() {
        return String.format(
                "pong monitoring configuration:"       +
                "\n\t - check frequency --- every %d %s"    +
                "\n\t - max age ----------- %d %s",
                monitoringConfiguration.getFrequency(), monitoringConfiguration.getTimeunit(),
                monitoringConfiguration.getMaxAge(),    monitoringConfiguration.getTimeunit()
        );
    }
}
