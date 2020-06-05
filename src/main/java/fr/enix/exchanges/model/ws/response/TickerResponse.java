package fr.enix.exchanges.model.ws.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TickerResponse extends ErrorResponse {

    private Map<String, Ticker> result;

    @Getter
    @Builder
    public static class Ticker {
        private List<String> a;
        private List<String> b;
        private List<String> l;
        private List<String> h;
    }

}
