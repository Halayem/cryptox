package fr.enix.exchanges.repository;

import fr.enix.exchanges.model.websocket.response.ChannelResponse;
import reactor.core.publisher.Mono;

public interface ChannelRepository {
    void saveChannel(final ChannelResponse channelResponse);
    Mono<String> getMarketAssetPairByChannelId(final Long channelId);
}
