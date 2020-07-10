package fr.enix.exchanges.model.parameters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssetTest {

    @Test
    public void testFindAssetByStringValue_success() {
        assertEquals(Asset.EUR, Asset.find("EUR"));
        assertEquals(Asset.LTC, Asset.find("LTC"));
    }

    @Test
    public void testFindAssetByStringValue_shouldThrowIllegalStateExceptionWhenValueIsUnknownByEnum() {
       assertThrows(IllegalStateException.class, () -> Asset.find("UNKNOWN_ASSET"));
    }
}
