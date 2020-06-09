package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.websocket.response.ChannelResponse;
import fr.enix.exchanges.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ChannelRepositoryInMemoryRepositoryImpl implements ChannelRepository {

    private final Map<Long, ChannelResponse> activeChannels;

    public ChannelRepositoryInMemoryRepositoryImpl() {
        activeChannels = new HashMap<>();
        log.info( "channel repository in memory was setup" );
    }

    @Override
    public void insertNewChannel(final ChannelResponse channelResponse) {
        activeChannels.put( channelResponse.getChannelID(), channelResponse );
    }

    @Override
    public ChannelResponse getActiveChannelById(final Long channelId) {
        return activeChannels.get(channelId);
    }
}
