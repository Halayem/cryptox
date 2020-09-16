package fr.enix.exchanges.monitor;

public interface HeartbeatMonitor {

    void start();
    void stop();
    boolean isHeartbeatError();
}
