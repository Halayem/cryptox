package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.repository.Ticker;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TickerRepository extends ReactiveCrudRepository<Ticker, Long> {
}
