package fr.enix.exchanges.model.ws.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class OpenOrdersResponse extends ErrorResponse {

    private Result result;

    /**
     * key is the transaction id
     */
    @Getter
    public static class Result {
        private Map<String, Order> open;
    }

    @Getter
    @ToString
    public static class Order {
        private Description descr;
        private String      status;
        private BigDecimal  vol;
    }

    @Getter
    @ToString
    public static class Description {
        private String      pair;
        private String      type;
        private BigDecimal  price;
    }
}
