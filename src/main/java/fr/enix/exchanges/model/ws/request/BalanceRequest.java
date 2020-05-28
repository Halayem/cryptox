package fr.enix.exchanges.model.ws.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BalanceRequest {
    private String nonce;

    public String getQueryParametersRepresentation() {
        return new StringBuilder("nonce").append("=").append(nonce).toString();
    }
}
