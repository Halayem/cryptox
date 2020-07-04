package fr.enix.exchanges.model.business.output;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class AddOrderOutput {

    private List<String> errors;
    private String description;
    private List<String> transactionIds;

}
