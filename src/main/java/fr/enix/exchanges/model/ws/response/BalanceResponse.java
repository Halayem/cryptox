package fr.enix.exchanges.model.ws.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class BalanceResponse {
    private List<String> error;
    private Map<String, BigDecimal> result;

}
