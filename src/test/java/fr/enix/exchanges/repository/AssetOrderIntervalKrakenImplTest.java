package fr.enix.exchanges.repository;

import fr.enix.exchanges.repository.impl.AssetOrderIntervalRepositoryKrakenImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AssetOrderIntervalKrakenImplTest {

    @Autowired
    private AssetOrderIntervalRepositoryKrakenImpl assetOrderIntervalRepositoryKraken;

    @Test
    void testGetMinimumOrderForLitecoinApplicationAsset() {
        assertEquals(new BigDecimal("0.1"), assetOrderIntervalRepositoryKraken.getMinimumOrderForApplicationAsset("litecoin"));
    }

    @Test
    void testGetMinimumOrderForUnknownApplicationAsset_shouldThrowRuntimeException() {
        Exception e = assertThrows( RuntimeException.class,
                                    () -> assetOrderIntervalRepositoryKraken.getMinimumOrderForApplicationAsset("unknownAsset"));
        assertEquals( "application asset: unknownAsset is not configured", e.getMessage() );
    }
}
