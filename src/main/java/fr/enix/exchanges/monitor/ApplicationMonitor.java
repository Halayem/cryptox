package fr.enix.exchanges.monitor;

public interface ApplicationMonitor {

    void start();
    void stop();
    boolean isError();
}
