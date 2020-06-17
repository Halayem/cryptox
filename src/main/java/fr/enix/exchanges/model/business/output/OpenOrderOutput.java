package fr.enix.exchanges.model.business.output;

import fr.enix.kraken.AddOrderType;
import fr.enix.kraken.AssetPair;
import fr.enix.kraken.Status;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class OpenOrderOutput {

    private String transactionId;
    private Status status;
    private AssetPair assetPair;
    private AddOrderType orderType;
    private BigDecimal price;
    private BigDecimal volume;
}
