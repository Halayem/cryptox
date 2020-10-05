package fr.enix.exchanges.model.business.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class BalanceOutput {

    private Map<String, BigDecimal> balances;
}
