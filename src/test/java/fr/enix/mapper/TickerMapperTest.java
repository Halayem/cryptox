package fr.enix.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.model.websocket.response.TickerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TickerMapperTest {

    @Autowired
    private TickerMapper tickerMapper;

    @Test
    void testMapStringToTickerResponse_success() throws JsonProcessingException {
        final TickerResponse tickerResponse = tickerMapper.mapStringToTickerResponse(
                "[379," +

                        "{\"a\":[\"41.41000\",36,\"36.79285097\"]," +
                        "\"b\":[\"41.35000\",281,\"281.90997402\"]," +
                        "\"c\":[\"41.36000\",\"0.50000000\"]," +
                        "\"v\":[\"1886.48997185\",\"14011.68602467\"]," +
                        "\"p\":[\"41.23445\",\"41.72934\"]," +
                        "\"t\":[224,1320]," +
                        "\"l\":[\"40.89000\",\"40.89000\"]," +
                        "\"h\":[\"41.50000\",\"42.29000\"]," +
                        "\"o\":[\"41.50000\",\"41.90000\"]}," +

                        "\"ticker\"," +
                        "\"LTC/EUR\"]"
        );
        assertEquals(379, tickerResponse.getChannelId());
        assertEquals("LTC/EUR", tickerResponse.getAssetPair() );

        assertEquals(3,             tickerResponse.getTicker().getA().size() );
        assertEquals("41.41000",    tickerResponse.getTicker().getA().get(0) );
        assertEquals("36",          tickerResponse.getTicker().getA().get(1) );
        assertEquals("36.79285097", tickerResponse.getTicker().getA().get(2) );

        assertEquals(3,             tickerResponse.getTicker().getB().size() );
        assertEquals("41.35000",    tickerResponse.getTicker().getB().get(0) );
        assertEquals("281",          tickerResponse.getTicker().getB().get(1) );
        assertEquals("281.90997402", tickerResponse.getTicker().getB().get(2) );

        assertEquals(2,             tickerResponse.getTicker().getC().size() );
        assertEquals("41.36000",    tickerResponse.getTicker().getC().get(0) );
        assertEquals("0.50000000",  tickerResponse.getTicker().getC().get(1) );

        assertEquals(2,                 tickerResponse.getTicker().getV().size() );
        assertEquals("1886.48997185",   tickerResponse.getTicker().getV().get(0) );
        assertEquals("14011.68602467",  tickerResponse.getTicker().getV().get(1) );

        assertEquals(2,                 tickerResponse.getTicker().getP().size() );
        assertEquals("41.23445",        tickerResponse.getTicker().getP().get(0) );
        assertEquals("41.72934",        tickerResponse.getTicker().getP().get(1) );

        assertEquals(2,             tickerResponse.getTicker().getT().size() );
        assertEquals("224",         tickerResponse.getTicker().getT().get(0) );
        assertEquals("1320",        tickerResponse.getTicker().getT().get(1) );

        assertEquals(2,             tickerResponse.getTicker().getL().size() );
        assertEquals("40.89000",    tickerResponse.getTicker().getL().get(0) );
        assertEquals("40.89000",    tickerResponse.getTicker().getL().get(1) );

        assertEquals(2,             tickerResponse.getTicker().getH().size() );
        assertEquals("41.50000",    tickerResponse.getTicker().getH().get(0) );
        assertEquals("42.29000",    tickerResponse.getTicker().getH().get(1) );

        assertEquals(2,             tickerResponse.getTicker().getO().size() );
        assertEquals("41.50000",    tickerResponse.getTicker().getO().get(0) );
        assertEquals("41.90000",    tickerResponse.getTicker().getO().get(1) );
    }

    @Test
    void testMapTickerResponseToTickerOutput_success(){
        StepVerifier
        .create         (tickerMapper.mapTickerResponseToTickerOutput(buildTickerResponse()))
        .consumeNextWith(tickerOutput -> {
            assertEquals( new BigDecimal("41.93000"),   tickerOutput.getAsk().getPrice() );
            assertEquals( new BigDecimal("156"),        tickerOutput.getAsk().getWholeLotVolume() );
            assertEquals( new BigDecimal("156.000"),    tickerOutput.getAsk().getLotVolume() );

            assertEquals( new BigDecimal("41.87000"),   tickerOutput.getBid().getPrice() );
            assertEquals( new BigDecimal("26"),         tickerOutput.getBid().getWholeLotVolume() );
            assertEquals( new BigDecimal("126.000"),    tickerOutput.getBid().getLotVolume() );

            assertEquals( new BigDecimal("41.71000"),   tickerOutput.getLowTrade().getToday() );
            assertEquals( new BigDecimal("41.46000"),   tickerOutput.getLowTrade().getLast24Hours() );

            assertEquals( new BigDecimal("42.33000"),   tickerOutput.getHighTrade().getToday() );
            assertEquals( new BigDecimal("42.81000"),   tickerOutput.getHighTrade().getLast24Hours() );
        }).verifyComplete();
    }

    private TickerResponse buildTickerResponse() {
        return TickerResponse.builder()
                             .ticker(TickerResponse.Ticker.builder()
                                                          .a( Arrays.asList( "41.93000", "156",  "156.000" ))
                                                          .b( Arrays.asList( "41.87000", "26",   "126.000" ))
                                                          .l( Arrays.asList( "41.71000", "41.46000" ))
                                                          .h( Arrays.asList( "42.33000", "42.81000" ))
                                                          .build()
                             )
                            .assetPair("LTC/EUR")
                            .build();
    }

}
