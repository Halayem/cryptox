package fr.enix.exchanges.model.websocket.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TickerResponse {

    private Integer channelId;
    private Ticker ticker;
    private String assetPair;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ticker {
        private List<String> a;
        private List<String> b;
        private List<String> c;
        private List<String> v;
        private List<String> p;
        private List<String> t;
        private List<String> o;
        private List<String> l;
        private List<String> h;
    }

}
