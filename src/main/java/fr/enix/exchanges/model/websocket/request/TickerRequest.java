package fr.enix.exchanges.model.websocket.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TickerRequest {

    private String event;
    private List<String> pair;
    private Subscription subscription;

    @Getter
    @Builder
    public static class Subscription {
        private String name;
    }
}
