package fr.enix.exchanges.model.business;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AddOrderOutput {

    private String description;
    private List<String> transactionIds;

}
