package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.exchanges.model.websocket.response.ChannelResponse;
import fr.enix.exchanges.service.ChannelService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChannelManager implements WebSocketSubscriptionManager  {
private final ChannelService channelService;

    @Override
    public void managePayload(final String payload) throws JsonProcessingException {
        channelService.saveChannel(payload);
    }
}
