/**
 * The exhaustive list of assets code and their interpretation {@link https://support.kraken.com/hc/en-us/articles/360001185506-How-to-interpret-asset-codes}
 */
package fr.enix.exchanges.model.parameters;

public enum Asset {
    EUR, LTC;

    public static Asset find(final String value) {
        try {
            return Asset.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                String.format("this value: %s is unknown by enum: %s ", value, Asset.class.getName())
            );
        }
    }
}
