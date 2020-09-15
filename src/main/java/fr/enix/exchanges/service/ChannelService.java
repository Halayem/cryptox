package fr.enix.exchanges.service;

import fr.enix.exchanges.model.websocket.response.ChannelResponse;

public interface ChannelService {

    void saveNewChannel(final ChannelResponse channelResponse);
}
