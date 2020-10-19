package fr.enix.endpoint;

import fr.enix.exchanges.model.dto.PriceReference;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@Api(
    value = "/v1/price-references",
    tags = "price-references",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public interface PriceReferenceEndpoint {
    @ApiOperation( value = "Update price reference")
    @ApiResponses({
        @ApiResponse( code = 200, message = "price reference updated for given application asset pair"),
        @ApiResponse( code = 404, message = "unknown application asset pair, or not configured to trade with bearing strategy"),
        @ApiResponse( code = 401, message = "you're not authorized to do this operation")
    })
    Mono<Void> updatePriceReferenceForApplicationAssetPair(
        @ApiParam(value = "Application Asset Pair to update", required = true) final PriceReference priceReference
    );

    @ApiOperation( value = "Get price reference for a given application asset pair")
    @ApiResponses({
        @ApiResponse( code = 200, message = "The current price reference by application asset pair", response = PriceReference.class),
        @ApiResponse( code = 404, message = "unknown application asset pair, or not configured to trade with bearing strategy"),
        @ApiResponse( code = 401, message = "you're not authorized to do this operation")
    })
    Mono<PriceReference> getPriceReferenceByApplicationAssetPair(@ApiParam(value = "Application Asset Pair",required = true) final String applicationAssetPair);

}
