package fr.enix.exchanges.repository;

import java.math.BigDecimal;

public interface AssetOrderIntervalRepository {

    BigDecimal getMinimumOrderForApplicationAsset(final String applicationAssetPair);
}
