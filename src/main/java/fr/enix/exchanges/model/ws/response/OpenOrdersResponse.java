package fr.enix.exchanges.model.ws.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class OpenOrdersResponse extends ErrorResponse {

    private Result result;

    @Getter
    public static class Result {
        private Map<String, Order> open;
    }

    @Getter
    public static class Order {

        private Description descr;
        private BigDecimal vol;
    }

    @Getter
    public static class Description {
        private String pair;
        private String type;
        private BigDecimal price;
    }
}
