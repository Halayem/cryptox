package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.model.websocket.response.ChannelResponse;
import fr.enix.exchanges.repository.ChannelRepository;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class ChannelRepositoryInMemoryRepositoryImpl implements ChannelRepository {

    private final Map<Long, ChannelResponse> channels = new ConcurrentHashMap<>();

    @Override
    public void saveChannel(final ChannelResponse channelResponse) {
        channels.put( channelResponse.getChannelID(), channelResponse );
    }

    @Override
    public Mono<String> getMarketAssetPairByChannelId(final Long channelId) {
        return Mono.justOrEmpty(channels.get(channelId).getPair());
    }
}
