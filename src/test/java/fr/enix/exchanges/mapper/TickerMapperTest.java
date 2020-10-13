package fr.enix.exchanges.mapper;

import fr.enix.common.utils.file.ApplicationFileUtils;
import fr.enix.exchanges.mapper.TickerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TickerMapperTest {

    @Autowired
    private TickerMapper tickerMapper;

    @Test
    void testMapTickerResponseToTickerOutput_success() throws IOException, URISyntaxException {
        StepVerifier
        .create         (tickerMapper.mapStringToTickerOutput(ApplicationFileUtils.getStringFileContentFromResources( "kraken/ticker/ltc_eur.json" )))
        .consumeNextWith(tickerOutput -> {
            assertEquals( new BigDecimal("41.41000"),       tickerOutput.getAsk().getPrice() );
            assertEquals( new BigDecimal("36"),             tickerOutput.getAsk().getWholeLotVolume() );
            assertEquals( new BigDecimal("36.79285097"),    tickerOutput.getAsk().getLotVolume() );

            assertEquals( new BigDecimal("41.35000"),       tickerOutput.getBid().getPrice() );
            assertEquals( new BigDecimal("281"),            tickerOutput.getBid().getWholeLotVolume() );
            assertEquals( new BigDecimal("281.90997402"),   tickerOutput.getBid().getLotVolume() );

            assertEquals( new BigDecimal("40.89000"), tickerOutput.getLowTrade().getToday() );
            assertEquals( new BigDecimal("40.89000"), tickerOutput.getLowTrade().getLast24Hours() );

            assertEquals( new BigDecimal("41.50000"), tickerOutput.getHighTrade().getToday() );
            assertEquals( new BigDecimal("42.29000"), tickerOutput.getHighTrade().getLast24Hours() );
        }).verifyComplete();
    }

}
