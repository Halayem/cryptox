package fr.enix.exchanges.model.business.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CancelOrderOutput {

    private Integer totalCanceledOrder;
}
