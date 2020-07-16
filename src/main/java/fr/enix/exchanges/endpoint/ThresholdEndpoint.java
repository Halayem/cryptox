package fr.enix.exchanges.endpoint;

import fr.enix.exchanges.model.dto.Threshold;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@Api( value = "/v1/thresholds",
      tags = "threshold",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
)
public interface ThresholdEndpoint {

    @ApiOperation( value = "Update threshold for litecoin currency trading")
    @ApiResponses({ @ApiResponse( code = 200,
                                  message = "threshold updated for litecoin trading"),
                    @ApiResponse( code = 401,
                                  message = "you're not authorized to do this operation")})
    Mono<Void> updateThreshold(@ApiParam(value = "Threshold to update",
                                         required = true)
                               final Threshold threshold
    );

    @ApiOperation( value = "Get threshold for litecoin currency trading")
    @ApiResponses({ @ApiResponse( code = 200,
                                 message = "current threshold for litecoin trading",
                                 response = Threshold.class),
                    @ApiResponse( code = 401,
                    message = "you're not authorized to do this operation")})
    Mono<Threshold> getThreshold();
}
