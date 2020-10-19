package fr.enix.exchanges.controller;

import fr.enix.endpoint.PriceReferenceEndpoint;
import fr.enix.exchanges.mapper.PriceReferenceMapper;
import fr.enix.exchanges.model.dto.PriceReference;
import fr.enix.exchanges.service.PriceReferenceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/v1/price-references", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PriceReferenceController implements PriceReferenceEndpoint {

    private final PriceReferenceService priceReferenceService;
    private final PriceReferenceMapper priceReferenceMapper;

    @Override
    @PostMapping
    public Mono<Void> updatePriceReferenceForApplicationAssetPair(@RequestBody final PriceReference priceReference) {
        priceReferenceService.updatePriceReference(priceReference);
        return Mono.empty();
    }

    @Override
    @RequestMapping( path = "/{applicationAssetPair}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PriceReference> getPriceReferenceByApplicationAssetPair(@PathVariable("applicationAssetPair") final String applicationAssetPair) {
        return  priceReferenceService
                .getPriceReferenceForApplicationAssetPair( applicationAssetPair )
                .map( priceReferenceMapper::mapFromRepositoryToDto );
    }
}
