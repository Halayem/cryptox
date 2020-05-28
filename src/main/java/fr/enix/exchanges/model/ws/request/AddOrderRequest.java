package fr.enix.exchanges.model.ws.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class AddOrderRequest {

    private String nonce;
    private String pair;
    private String type;
    private String ordertype;
    private BigDecimal price;
    private BigDecimal volume;

    public String getQueryParametersRepresentation() {
        return new StringBuilder("nonce"    ).append("=").append(nonce      ).append("&")
                         .append("pair"     ).append("=").append(pair       ).append("&")
                         .append("type"     ).append("=").append(type       ).append("&")
                         .append("ordertype").append("=").append(ordertype  ).append("&")
                         .append("price"    ).append("=").append(price      ).append("&")
                         .append("volume"   ).append("=").append(volume     ).toString();
    }
}
