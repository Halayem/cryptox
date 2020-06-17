package fr.enix.exchanges.model.business.input;

import fr.enix.kraken.AddOrderType;
import fr.enix.kraken.AssetPair;
import fr.enix.kraken.OrderType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class AddOrderInput {

    private AssetPair assetPair;
    private AddOrderType addOrderType;
    private OrderType orderType;
    private BigDecimal price;
    private BigDecimal volume;
}
