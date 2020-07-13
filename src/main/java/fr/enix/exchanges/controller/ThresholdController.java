package fr.enix.exchanges.controller;

import fr.enix.exchanges.endpoint.ThresholdEndpoint;
import fr.enix.exchanges.mapper.ThresholdMapper;
import fr.enix.exchanges.model.dto.Threshold;
import fr.enix.exchanges.service.FixedThresholdService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/v1/thresholds",
                produces = MediaType.APPLICATION_JSON_VALUE
)
@AllArgsConstructor
public class ThresholdController implements ThresholdEndpoint {

    private final FixedThresholdService fixedThresholdService;
    private final ThresholdMapper thresholdMapper;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> updateThreshold(@RequestBody final Threshold threshold) {
        fixedThresholdService.updateThresholds(threshold);
        return Mono.empty();
    }

    @Override
    @GetMapping("/{asset}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Threshold> getThreshold() {
        return fixedThresholdService.getThresholds()
                                    .map(threshold -> thresholdMapper.mapFromRepositoryToDto(threshold));
    }
}
