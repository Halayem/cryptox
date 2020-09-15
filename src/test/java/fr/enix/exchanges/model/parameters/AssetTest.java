package fr.enix.exchanges.model.parameters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssetTest {

    @Test
    void testFindAssetByStringValue_success() {
        assertEquals(Asset.EUR, Asset.find("EUR"));
        assertEquals(Asset.LTC, Asset.find("LTC"));
    }

    @Test
    void testFindAssetByStringValue_shouldThrowIllegalStateExceptionWhenValueIsUnknownByEnum() {
       assertThrows(IllegalStateException.class, () -> Asset.find("UNKNOWN_ASSET"));
    }
}
