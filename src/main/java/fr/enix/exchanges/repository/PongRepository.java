package fr.enix.exchanges.repository;

import java.time.LocalDateTime;

public interface PongRepository {

    void  savePongDatetime();
    LocalDateTime getLastPongDatetime();
}
