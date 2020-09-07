package fr.enix.exchanges.model.ws.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriUtils;

import java.math.BigDecimal;

@Builder
@Getter
@EqualsAndHashCode
@Slf4j
@ToString
public class AddOrderRequest {

    private String      nonce;
    private String      pair;
    private String      type;
    private String      ordertype;
    private BigDecimal  price;
    private BigDecimal  volume;
    private String      leverage;
    private Close       close;

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Close {
        private String ordertype;
        private String price;
        private String price2;
    }

    public String getQueryParametersRepresentation() {
        StringBuilder stringBuilder = new StringBuilder("nonce"    ).append("=").append(nonce       ).append("&")
                                                .append("pair"     ).append("=").append(pair        ).append("&")
                                                .append("type"     ).append("=").append(type        ).append("&")
                                                .append("ordertype").append("=").append(ordertype   ).append("&")
                                                .append("volume"   ).append("=").append(volume      );

        if ( price != null ) {
            stringBuilder.append("&").append("price").append("=").append(price);
        }
        if ( leverage != null ) {
            stringBuilder.append("&").append("leverage").append("=").append(leverage);
        }
        if ( close != null ) {
            stringBuilder.append("&").append("close[ordertype]" ).append("=").append(close.ordertype)
                         .append("&").append("close[price]"     ).append("=").append(close.price    )
                         .append("&").append("close[price2]"    ).append("=").append(close.price2   );
        }

        log.info(
            "built add order request: {}, encoder: {}",
            stringBuilder.toString(),
            UriUtils.encodePath(stringBuilder.toString(), "UTF-8")
        );
        return UriUtils.encodePath(stringBuilder.toString(), "UTF-8");
    }
}
