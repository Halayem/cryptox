package fr.enix.mapper;

import fr.enix.exchanges.model.business.TickerOutput;
import fr.enix.exchanges.model.ws.response.TickerResponse;
import fr.enix.kraken.AssetPair;

import java.math.BigDecimal;

public class TickerMapper {

    public TickerOutput mapTickerResponseToTickerOutput(final TickerResponse tickerResponse,
                                                        final AssetPair assetPair) {

        final TickerResponse.Ticker ticker = tickerResponse.getResult().get(assetPair.getCode());
        return TickerOutput.builder ()
                           .ask(TickerOutput.Offer.builder()
                                                  .price         (new BigDecimal(ticker.getA().get(0)))
                                                  .wholeLotVolume(new BigDecimal(ticker.getA().get(1)))
                                                  .lotVolume     (new BigDecimal(ticker.getA().get(2)))
                                                  .build()
                           )
                           .bid(TickerOutput.Offer.builder()
                                                  .price         (new BigDecimal(ticker.getB().get(0)))
                                                  .wholeLotVolume(new BigDecimal(ticker.getB().get(1)))
                                                  .lotVolume     (new BigDecimal(ticker.getB().get(2)))
                                                  .build()
                           )
                           .lowTrade( TickerOutput.MarketStatus.builder()
                                                               .today       (new BigDecimal(ticker.getL().get(0)))
                                                               .last24Hours (new BigDecimal(ticker.getL().get(1)))
                                                               .build()
                           )
                           .highTrade( TickerOutput.MarketStatus.builder()
                                                                .today       (new BigDecimal(ticker.getH().get(0)))
                                                                .last24Hours (new BigDecimal(ticker.getH().get(1)))
                                                                .build()
                           )
                           .build();
    }

}
