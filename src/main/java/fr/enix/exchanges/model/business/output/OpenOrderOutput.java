package fr.enix.exchanges.model.business.output;

import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@ToString
public class OpenOrderOutput {

    private String transactionId;
    private Status status;
    private String assetPair;
    private AddOrderType orderType;
    private BigDecimal price;
    private BigDecimal volume;
}
