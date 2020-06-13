package fr.enix.exchanges.model.websocket.response;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChannelResponse {
    private Long channelID;
    private String channelName;
    private String event;
    private String pair;
    private String status;
    private Subscription subscription;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Subscription {
        private String name;
    }
}
