package fr.enix.exchanges.repository.impl.kraken;

import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KrakenAssetOrderIntervalRepositoryImpl implements AssetOrderIntervalRepository {

    // key is application asset pair
    private Map<String, BigDecimal> data = new HashMap<>();

    public KrakenAssetOrderIntervalRepositoryImpl() {
        data.put("litecoin-euro", new BigDecimal("0.1"));
    }

    @Override
    public BigDecimal getMinimumOrderForApplicationAsset(final String applicationAssetPair) {
        if( !data.containsKey(applicationAssetPair) ) {
            throw new IllegalArgumentException("application asset: " + applicationAssetPair + " is not configured");
        }
        return data.get(applicationAssetPair);
    }
}
