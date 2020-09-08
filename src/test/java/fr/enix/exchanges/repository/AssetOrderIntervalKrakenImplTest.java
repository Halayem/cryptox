package fr.enix.exchanges.repository;

import fr.enix.exchanges.repository.impl.AssetOrderIntervalRepositoryKrakenImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AssetOrderIntervalKrakenImplTest {

    @Autowired
    private AssetOrderIntervalRepositoryKrakenImpl assetOrderIntervalRepositoryKraken;

    @Test
    public void testGetMinimumLitecoinOrder_shouldBeZeroDotOne() {
        assertEquals(new BigDecimal("0.1"), assetOrderIntervalRepositoryKraken.getMinimumLitecoinOrder());
    }

    @Test
    public void testGetMinimumEuroOrder_shouldBeTen() {
        assertEquals(new BigDecimal("10"), assetOrderIntervalRepositoryKraken.getMinimumEuroOrder());
    }
}
