package fr.enix.exchanges.model.ws.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AddOrderResponse {

    private List<String> error;
    private Result result;

    @Getter
    public static class Result {
        private Description descr;
        private List<String> txid;
    }

    @Getter
    public static class Description {
        private String order;
    }
}
