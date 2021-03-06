package fr.enix.exchanges.mapper.impl.kraken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.model.business.output.TickerOutput;
import fr.enix.exchanges.model.websocket.response.TickerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public class KrakenTickerMapperImpl implements TickerMapper {

    @Override
    public Mono<TickerOutput> mapStringToTickerOutput(final String payload) throws JsonProcessingException {
        return
            mapStringToTickerResponse(payload)
            .map( tickerResponse ->
                    TickerOutput.builder()
                        .ask(
                            TickerOutput.Offer
                            .builder        ()
                            .price          (new BigDecimal(tickerResponse.getTicker().getA().get(0)))
                            .wholeLotVolume (new BigDecimal(tickerResponse.getTicker().getA().get(1)))
                            .lotVolume      (new BigDecimal(tickerResponse.getTicker().getA().get(2)))
                            .build          ()
                        )
                        .bid(
                            TickerOutput.Offer
                            .builder        ()
                            .price          (new BigDecimal(tickerResponse.getTicker().getB().get(0)))
                            .wholeLotVolume (new BigDecimal(tickerResponse.getTicker().getB().get(1)))
                            .lotVolume      (new BigDecimal(tickerResponse.getTicker().getB().get(2)))
                            .build          ()
                        )
                        .lowTrade(
                            TickerOutput.MarketStatus
                            .builder    ()
                            .today      (new BigDecimal(tickerResponse.getTicker().getL().get(0)))
                            .last24Hours(new BigDecimal(tickerResponse.getTicker().getL().get(1)))
                            .build      ()
                        )
                        .highTrade(
                            TickerOutput.MarketStatus
                            .builder    ()
                            .today      (new BigDecimal(tickerResponse.getTicker().getH().get(0)))
                            .last24Hours(new BigDecimal(tickerResponse.getTicker().getH().get(1)))
                            .build      ()
                        )
                        .assetPair(tickerResponse.getAssetPair())
                    .build()
                );
    }

    protected Mono<TickerResponse> mapStringToTickerResponse(final String payload) throws JsonProcessingException {
        final List<Object> objects = new ObjectMapper().readValue(payload, List.class);
        return Mono.just(
                    TickerResponse
                    .builder    ()
                    .channelId  ( (Integer) objects.get(0) )
                    .ticker     ( new ObjectMapper().convertValue(objects.get(1), TickerResponse.Ticker.class ))
                    .assetPair  ( (String)objects.get(3) )
                    .build      ()
        );
    }
}
