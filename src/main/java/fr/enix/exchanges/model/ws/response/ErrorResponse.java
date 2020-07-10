package fr.enix.exchanges.model.ws.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponse {
    private List<String> error;
}
