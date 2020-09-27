package fr.enix.exchanges.model.business.input;

import fr.enix.exchanges.model.websocket.AssetPair;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.OrderType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@ToString
public class AddOrderInput {

    private String          applicationAssetPair;
    private AddOrderType    addOrderType;
    private OrderType       orderType;
    private BigDecimal      volume;
    private BigDecimal      price;
    private String          leverage;
    private Close           close;

    @Builder
    @Getter
    public static class Close {
        private OrderType   orderType;
        private Integer     stopLossPriceRelativePercentageDelta;
        private Integer     takeProfitPriceRelativeDelta;
    }

}
