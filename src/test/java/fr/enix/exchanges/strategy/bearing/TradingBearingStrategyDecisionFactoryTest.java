package fr.enix.exchanges.strategy.bearing;

import fr.enix.exchanges.strategy.bearing.impl.DoNothingTradingBearingStrategyDecisionImpl;
import fr.enix.exchanges.strategy.bearing.impl.HighGapTradingBearingStrategyDecisionImpl;
import fr.enix.exchanges.strategy.bearing.impl.LowGapTradingBearingStrategyDecisionImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class TradingBearingStrategyDecisionFactoryTest {

    @Autowired
    private TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactory;

    @Test
    void testGetTradingBearingStrategyInstance_shouldReturnLowGapInstance() {
        final TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactorySpy = spy(tradingBearingStrategyDecisionFactory);
        doReturn( TradingBearingStrategyDecisionFactory.GapStatus.LOW_REACHED)
        .when   ( tradingBearingStrategyDecisionFactorySpy).getGapStatus(any(), any(), any());

        assertTrue(tradingBearingStrategyDecisionFactorySpy.getTradingBearingStrategyInstance(any(), any(), any()) instanceof LowGapTradingBearingStrategyDecisionImpl);

    }

    @Test
    void testGetTradingBearingStrategyInstance_shouldReturnHighGapInstance() {
        final TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactorySpy = spy(tradingBearingStrategyDecisionFactory);
        doReturn( TradingBearingStrategyDecisionFactory.GapStatus.HIGH_REACHED)
        .when   ( tradingBearingStrategyDecisionFactorySpy).getGapStatus(any(), any(), any());

        assertTrue(tradingBearingStrategyDecisionFactorySpy.getTradingBearingStrategyInstance(any(), any(), any()) instanceof HighGapTradingBearingStrategyDecisionImpl);

    }

    @Test
    void testGetTradingBearingStrategyInstance_shouldReturnDoNothingInstance() {
        final TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactorySpy = spy(tradingBearingStrategyDecisionFactory);
        doReturn( TradingBearingStrategyDecisionFactory.GapStatus.NOT_REACHED)
        .when   ( tradingBearingStrategyDecisionFactorySpy).getGapStatus(any(), any(), any());

        assertTrue(tradingBearingStrategyDecisionFactorySpy.getTradingBearingStrategyInstance(any(), any(), any()) instanceof DoNothingTradingBearingStrategyDecisionImpl);

    }

}
