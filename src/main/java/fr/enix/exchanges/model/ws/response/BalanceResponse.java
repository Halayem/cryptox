package fr.enix.exchanges.model.ws.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class BalanceResponse extends ErrorResponse {

    /**
     * key is kraken application asset pair
     */
    private Map<String, BigDecimal> result;

}
