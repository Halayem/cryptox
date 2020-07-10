package fr.enix.exchanges.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TickerServiceImplTest {

    @Autowired private TickerServiceImpl tickerServiceImpl;

    @Test
    public void testComputeBuyVolume_shouldDivideEuroVolumeTradingUnitByAskPriceWhenAvailableAssetIsGreaterThanTradingUnit() {
        assertEquals
        (
            new BigDecimal("0.57142857"),
            tickerServiceImpl.computeBuyVolume(new BigDecimal("25.00"), new BigDecimal("35.00"))
        );
    }

    @Test
    public void testComputeBuyVolume_shouldDivideAvailableAssetByAskPriceWhenAvailableAssetIsLessThanTradingUnit() {
        assertEquals
                (
                        new BigDecimal("0.51232792"),
                        tickerServiceImpl.computeBuyVolume(new BigDecimal("18.52"), new BigDecimal("36.14872254"))
                );
    }
}
