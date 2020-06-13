package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.websocket.response.ChannelResponse;

public interface ChannelRepository {
    void insertNewChannel(final ChannelResponse channelResponse);
    ChannelResponse getActiveChannelById(final Long channelId);
}
