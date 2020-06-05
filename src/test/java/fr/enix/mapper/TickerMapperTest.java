package fr.enix.mapper;

import fr.enix.exchanges.model.business.TickerOutput;
import fr.enix.exchanges.model.ws.response.TickerResponse;
import fr.enix.kraken.AssetPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TickerMapperTest {

    @Autowired
    private TickerMapper tickerMapper;

    @Test
    public void testMapTickerResponseToTickerOutput_success(){
        final TickerOutput tickerOutput = tickerMapper.mapTickerResponseToTickerOutput(
                                                buildTickerResponse(),
                                                AssetPair.LITECOIN_TO_EURO
                                            );
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

    }

    private TickerResponse buildTickerResponse() {

        return TickerResponse.builder().result(
                new HashMap<String, TickerResponse.Ticker>() {{
                    put(
                        AssetPair.LITECOIN_TO_EURO.getCode(),
                        TickerResponse.Ticker.builder()
                                             .a( Arrays.asList( "41.93000", "156",  "156.000" ))
                                             .b( Arrays.asList( "41.87000", "26",   "126.000" ))
                                             .l( Arrays.asList( "41.71000", "41.46000" ))
                                             .h( Arrays.asList( "42.33000", "42.81000" ))
                                             .build());
                }}
            ).build();

    }


}
