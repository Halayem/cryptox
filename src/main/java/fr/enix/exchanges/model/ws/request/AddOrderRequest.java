package fr.enix.exchanges.model.ws.request;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Builder()
@Getter
@Slf4j
public class AddOrderRequest {

    private String nonce;
    private String pair;
    private String type;
    private String ordertype;
    private BigDecimal price;
    private BigDecimal volume;

    public String getQueryParametersRepresentation() {
        StringBuilder stringBuilder = new StringBuilder("nonce"    ).append("=").append(nonce      ).append("&")
                                                .append("pair"     ).append("=").append(pair       ).append("&")
                                                .append("type"     ).append("=").append(type       ).append("&")
                                                .append("ordertype").append("=").append(ordertype  ).append("&")
                                                .append("volume"   ).append("=").append(volume     );

        if ( price != null ) {
            stringBuilder.append("&").append("price").append("=").append(price);
        }

        log.info("built add order request: {}", stringBuilder.toString());
        return stringBuilder.toString();
    }
}
