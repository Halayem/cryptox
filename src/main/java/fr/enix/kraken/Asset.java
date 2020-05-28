/**
 * The exhaustive list of assets code and their interpretation {@link https://support.kraken.com/hc/en-us/articles/360001185506-How-to-interpret-asset-codes}
 */
package fr.enix.kraken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Asset {
    EURO    ("ZEUR", AssetType.FIAT),
    USDOLLAR("ZUSD", AssetType.FIAT),

    BITCOIN ("XBTC",    AssetType.CRYPTOCURRENCY),
    LITECOIN("XLTC",    AssetType.CRYPTOCURRENCY),
    CARDANO ("ADA",     AssetType.CRYPTOCURRENCY);

    private String code;
    private AssetType type;
}
