package fr.enix.exchanges.model.ws.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TradeBalanceRequest {
    private String nonce;
    private String aclass;

    public String getQueryParametersRepresentation() {
        final StringBuilder sb = new StringBuilder("nonce").append("=").append(nonce);
        if(aclass != null ) {
            sb.append("&aclass").append("=").append(aclass);
        }
        return sb.toString();
    }
}
