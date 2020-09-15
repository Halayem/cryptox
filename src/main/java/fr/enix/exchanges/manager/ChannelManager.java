package fr.enix.exchanges.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.exchanges.model.websocket.response.ChannelResponse;
import fr.enix.exchanges.repository.ChannelRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChannelManager implements WebSocketSubscriptionManager  {

    private final ChannelRepository channelRepository;

    @Override
    public void managePayload(final String payload) throws JsonProcessingException {
        channelRepository.insertNewChannel(new ObjectMapper().readValue(payload, ChannelResponse.class));
    }
}
