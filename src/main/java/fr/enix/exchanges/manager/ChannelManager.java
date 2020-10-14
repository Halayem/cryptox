package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.service.ChannelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ChannelManager implements WebSocketSubscriptionManager  {
private final ChannelService channelService;

    @Override
    public void managePayload(final String payload) throws JsonProcessingException {
        log.debug("received payload: {}", payload);
        channelService.saveChannel(payload);
    }
}
