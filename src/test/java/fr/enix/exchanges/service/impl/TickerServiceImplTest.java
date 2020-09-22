package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TickerServiceImplTest {
    @Autowired private TickerService tickerService;

    /*
    @Test
    void testComputeBuyVolume_shouldDivideEuroVolumeTradingUnitByAskPriceWhenAvailableAssetIsGreaterThanTradingUnit() {
        assertEquals
        (
            new BigDecimal("0.57142857"),
            tickerServiceImpl.computeBuyVolume(new BigDecimal("25.00"), new BigDecimal("35.00"))
        );
    }

    @Test
    void testComputeBuyVolume_shouldDivideAvailableAssetByAskPriceWhenAvailableAssetIsLessThanTradingUnit() {
        assertEquals
                (
                        new BigDecimal("0.51232792"),
                        tickerServiceImpl.computeBuyVolume(new BigDecimal("18.52"), new BigDecimal("36.14872254"))
                );
    }

     */
}
