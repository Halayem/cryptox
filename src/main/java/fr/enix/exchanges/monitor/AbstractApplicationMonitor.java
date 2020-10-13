package fr.enix.exchanges.monitor;

import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractApplicationMonitor implements ApplicationMonitor {

    private static final long INITIAL_DELAY_IN_SECONDS = 1L;
    public void startMonitoring(final long checkFrequency, final ChronoUnit timeunit) {
        Executors
        .newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate(this::doVerify, INITIAL_DELAY_IN_SECONDS, checkFrequency, TimeUnit.of(timeunit));
    }

    @Override
    public void stop() {
        log.warn("stop monitor is not yet implemented");
    }

    public abstract void doVerify();
}
