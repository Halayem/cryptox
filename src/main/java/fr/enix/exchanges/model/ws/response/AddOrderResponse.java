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
public class AddOrderResponse extends ErrorResponse {

    private Result result;

    @Getter
    @ToString
    public static class Result {
        private Description descr;
        private List<String> txid;
    }

    @Getter
    @ToString
    public static class Description {
        private String order;
    }
}
