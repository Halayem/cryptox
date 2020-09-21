package fr.enix.exchanges.service.impl.kraken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.exchanges.model.websocket.response.ChannelResponse;
import fr.enix.exchanges.repository.ChannelRepository;
import fr.enix.exchanges.service.ChannelService;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
public class KrakenChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final CurrenciesRepresentationService currenciesRepresentationService;

    @Override
    public void saveChannel(final String payload) throws JsonProcessingException {
        channelRepository.saveChannel(new ObjectMapper().readValue(payload, ChannelResponse.class));
    }

    @Override
    public Mono<String> getApplicationAssetPairByChannelId(final Long channelId) {
        return channelRepository
                .getMarketAssetPairByChannelId(channelId)
                .map(currenciesRepresentationService::getApplicationAssetPairCurrencyRepresentationByMarketAssetPair);
    }
}
