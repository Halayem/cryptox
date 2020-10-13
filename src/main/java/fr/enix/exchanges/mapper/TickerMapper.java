package fr.enix.exchanges.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.model.business.output.TickerOutput;
import reactor.core.publisher.Mono;

public interface TickerMapper {
    Mono<TickerOutput> mapStringToTickerOutput(final String payload) throws JsonProcessingException;
}
