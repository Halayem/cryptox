package fr.enix.exchanges.model.ws.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private List<String> error;
}
