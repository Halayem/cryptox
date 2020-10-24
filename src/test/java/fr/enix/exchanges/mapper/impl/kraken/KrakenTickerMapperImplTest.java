package fr.enix.exchanges.mapper.impl.kraken;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.common.utils.file.ApplicationFileUtils;
import fr.enix.exchanges.model.websocket.response.TickerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Profile("kraken")
class KrakenTickerMapperImplTest {

    @Autowired
    private KrakenTickerMapperImpl krakenTickerMapper;
    private final String mockedTickerStringResponse;

    public KrakenTickerMapperImplTest() throws IOException, URISyntaxException {
        mockedTickerStringResponse  = ApplicationFileUtils.getStringFileContentFromResources( "kraken/ticker/ltc_eur.json" );
    }

    @Test
    void testMapStringToTickerResponse_channelIdShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals(379, tickerResponse.getChannelId());
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_marketAssetPairShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals("LTC/EUR", tickerResponse.getAssetPair() );
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_tickerAskShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals(3,             tickerResponse.getTicker().getA().size() );
            assertEquals("41.41000",    tickerResponse.getTicker().getA().get(0) );
            assertEquals("36",          tickerResponse.getTicker().getA().get(1) );
            assertEquals("36.79285097", tickerResponse.getTicker().getA().get(2) );
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_tickerBidShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals(3,                 tickerResponse.getTicker().getB().size() );
            assertEquals("41.35000",        tickerResponse.getTicker().getB().get(0) );
            assertEquals("281",             tickerResponse.getTicker().getB().get(1) );
            assertEquals("281.90997402",    tickerResponse.getTicker().getB().get(2) );

        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_lastClosedShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals(2,             tickerResponse.getTicker().getC().size() );
            assertEquals("41.36000",    tickerResponse.getTicker().getC().get(0) );
            assertEquals("0.50000000",  tickerResponse.getTicker().getC().get(1) );
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_volumeShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals(2,                 tickerResponse.getTicker().getV().size() );
            assertEquals("1886.48997185",   tickerResponse.getTicker().getV().get(0) );
            assertEquals("14011.68602467",  tickerResponse.getTicker().getV().get(1) );
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_volumeWeightedAveragePriceShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ))
        .consumeNextWith( tickerResponse -> {
            assertEquals(2,             tickerResponse.getTicker().getP().size() );
            assertEquals("41.23445",    tickerResponse.getTicker().getP().get(0) );
            assertEquals("41.72934",    tickerResponse.getTicker().getP().get(1) );
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_numberOfTradesShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals(2,             tickerResponse.getTicker().getT().size() );
            assertEquals("224",         tickerResponse.getTicker().getT().get(0) );
            assertEquals("1320",        tickerResponse.getTicker().getT().get(1) );
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_lowTodayAndLast24HoursShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals(2,             tickerResponse.getTicker().getL().size() );
            assertEquals("40.89000",    tickerResponse.getTicker().getL().get(0) );
            assertEquals("40.89000",    tickerResponse.getTicker().getL().get(1) );
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_highTodayAndLast24HoursShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create(krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith(tickerResponse -> {
            assertEquals(2,             tickerResponse.getTicker().getH().size());
            assertEquals("41.50000",    tickerResponse.getTicker().getH().get(0));
            assertEquals("42.29000",    tickerResponse.getTicker().getH().get(1));
        })
        .verifyComplete();
    }

    @Test
    void testMapStringToTickerResponse_todayOpeningPriceShouldBeMapped() throws JsonProcessingException {
        StepVerifier
        .create         ( krakenTickerMapper.mapStringToTickerResponse( mockedTickerStringResponse ) )
        .consumeNextWith( tickerResponse -> {
            assertEquals(2,             tickerResponse.getTicker().getO().size() );
            assertEquals("41.50000",    tickerResponse.getTicker().getO().get(0) );
            assertEquals("41.90000",    tickerResponse.getTicker().getO().get(1) );
        })
        .verifyComplete();
    }
}
