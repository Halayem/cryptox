package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.repository.HeartbeatRepository;

import java.time.LocalDateTime;

public class HeartbeatRepositoryInMemoryImpl implements HeartbeatRepository {

    private LocalDateTime heartbeatDatetime;

    @Override
    public synchronized void saveHeartbeatDatetime() { heartbeatDatetime = LocalDateTime.now(); }

    @Override
    public LocalDateTime getLastHeartbeatDatetime() { return heartbeatDatetime; }


}
