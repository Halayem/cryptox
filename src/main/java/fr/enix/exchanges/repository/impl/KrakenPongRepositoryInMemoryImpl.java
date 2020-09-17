package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.repository.PongRepository;

import java.time.LocalDateTime;

public class KrakenPongRepositoryInMemoryImpl implements PongRepository {

    private LocalDateTime pongDatetime;

    @Override
    public synchronized void savePongDatetime() {
        pongDatetime = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getLastPongDatetime() {
        return pongDatetime;
    }
}
