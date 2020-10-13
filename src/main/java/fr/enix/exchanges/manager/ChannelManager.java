package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
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
