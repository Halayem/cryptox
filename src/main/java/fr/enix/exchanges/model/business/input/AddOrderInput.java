package fr.enix.exchanges.model.business.input;

import fr.enix.exchanges.model.websocket.AssetPair;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.OrderType;
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
