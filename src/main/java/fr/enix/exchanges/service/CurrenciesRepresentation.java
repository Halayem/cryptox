package fr.enix.exchanges.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CurrenciesRepresentation {
    /**
     * @param 0: The X cryptocurrency or the Z fiat currency for Kraken market place
     *         Note from kraken.com: Asset codes starting with 'X' represent cryptocurrencies, though this convention is no longer followed for newer coin listings.
     */
    LITECOIN("XLTC",    "LTC" ),
    RIPPLE  ("XXRP",    "XRP" ),
    BITCOIN ("XXBT",    "XBT" ),
    SIACOIN ("SC",      "SC"  ),

    EURO        ("ZEUR", "EUR"),
    US_DOLLAR   ("ZUSD", "USD");

    private String krakenWebServiceRepresentation;
    private String krakenWebSocketRepresentation;
}
