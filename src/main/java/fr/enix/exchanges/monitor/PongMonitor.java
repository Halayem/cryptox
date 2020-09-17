package fr.enix.exchanges.monitor;

public interface PongMonitor {

    void start();
    void stop();
    boolean isHeartbeatError();
}
