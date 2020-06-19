package fr.enix.exchanges.model.business.output;

import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.Status;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class OpenOrderOutput {

    private String transactionId;
    private Status status;
    private String assetPair;
    private AddOrderType orderType;
    private BigDecimal price;
    private BigDecimal volume;
}
