package fr.enix.exchanges.model.repository;

import lombok.Getter;
import lombok.Setter;

import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class MonitoringConfiguration {
    private ChronoUnit timeunit;
    private long frequency;
    private long maxAge;
}
