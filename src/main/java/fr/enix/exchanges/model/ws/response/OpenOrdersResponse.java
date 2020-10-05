package fr.enix.exchanges.model.ws.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        private Description descr;
        private String      status;
        private BigDecimal  vol;
    }

    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Description {
        private String      pair;
        private String      type;
        private BigDecimal  price;
    }
}
