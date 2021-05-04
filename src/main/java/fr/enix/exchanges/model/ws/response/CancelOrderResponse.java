package fr.enix.exchanges.model.ws.response;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CancelOrderResponse extends ErrorResponse {

    private Result result;

    @NoArgsConstructor
    @Getter
    @ToString
    public static class Result {
        private Integer count;
    }
}
