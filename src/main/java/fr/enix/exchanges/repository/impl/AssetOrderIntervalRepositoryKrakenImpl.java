package fr.enix.exchanges.repository.impl;

import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Slf4j
public class AssetOrderIntervalRepositoryKrakenImpl implements AssetOrderIntervalRepository {

    @Override
    public BigDecimal getMinimumLitecoinOrder() {
        return zeroDotOne;
    }

    @Override
    public BigDecimal getMinimumEuroOrder() {
        return ten;
    }

    private final BigDecimal ten = new BigDecimal("10");
    private final BigDecimal zeroDotOne = new BigDecimal("0.1");
    /*
     * consider updating the post construct method when adding new maximum or minimum order
     */

    @PostConstruct
    private void traceBeanCreationConfiguration() {

        log.info(
            String.format(
                "dump of defined kraken minimum order"  + "\n\t" +
                "- litecoin -- %s"                      + "\n\t" +
                "- euro ------ %s",
                getMinimumLitecoinOrder(),
                getMinimumEuroOrder()
            )
        );
    }

}
