package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AssetOrderIntervalRepositoryKrakenImpl implements AssetOrderIntervalRepository {

    // key is application asset pair
    private Map<String, BigDecimal> data = new HashMap<>();

    public AssetOrderIntervalRepositoryKrakenImpl() {
        data.put("litecoin-euro", new BigDecimal("0.1"));
    }

    @Override
    public BigDecimal getMinimumOrderForApplicationAsset(final String applicationAssetPair) {
        if( !data.containsKey(applicationAssetPair) ) {
            throw new RuntimeException("application asset: " + applicationAssetPair + " is not configured");
        }
        return data.get(applicationAssetPair);
    }
}
