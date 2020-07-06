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
    private BigDecimal volume;

    // in case of sell
    private BigDecimal price;
    // in case of buy, by default is zero and mean now
    // private Long startTime;
}
