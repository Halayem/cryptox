package fr.enix.exchanges.model.ws.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CancelOrderRequest {
    private String nonce;
    private String txid;

    public String getQueryParametersRepresentation() {
        return new StringBuilder("nonce").append("=").append(nonce).append("&")
                         .append("txid" ).append("=").append(txid ).toString();
    }
}
