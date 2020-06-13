/**
 * The exhaustive list of assets code and their interpretation {@link https://support.kraken.com/hc/en-us/articles/360001185506-How-to-interpret-asset-codes}
 */
package fr.enix.kraken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Asset {
    EURO    ("ZEUR", "EUR", AssetType.FIAT),
    USDOLLAR("ZUSD", "USD", AssetType.FIAT),

    BITCOIN ("XBTC", "BTC", AssetType.CRYPTOCURRENCY),
    LITECOIN("XLTC", "LTC", AssetType.CRYPTOCURRENCY),
    CARDANO ("ADA",  "ADA", AssetType.CRYPTOCURRENCY);

    private String code;
    private String wsCode;
    private AssetType type;
}
