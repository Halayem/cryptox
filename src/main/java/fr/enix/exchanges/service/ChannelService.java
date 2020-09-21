package fr.enix.exchanges.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;

public interface ChannelService {

    void saveChannel(final String payload) throws JsonProcessingException;
    Mono<String> getApplicationAssetPairByChannelId(final Long channelId);
}
