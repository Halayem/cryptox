package fr.enix.exchanges.repository;

import java.time.LocalDateTime;

public interface HeartbeatRepository {

    void  saveHeartbeatDatetime();
    LocalDateTime getLastHeartbeatDatetime();
}
