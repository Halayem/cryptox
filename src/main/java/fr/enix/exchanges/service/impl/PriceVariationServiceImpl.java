package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.service.PriceVariationService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Slf4j
public class PriceVariationServiceImpl implements PriceVariationService  {

    @Override
    public boolean arePricesAboutEquals(final BigDecimal price1,
                                        final BigDecimal price2) {

        return  ( ! isOneArgIsNull( price1, price2 ) ) &&
                ( getScaledPrice( price1 ).equals( getScaledPrice ( price2 ) ) );
    }

    private BigDecimal getScaledPrice(final BigDecimal price) {
        return price.setScale( scale, roundingMode );
    }

    private boolean isOneArgIsNull(final Object ...objects) {
       return Arrays.asList(objects).contains(null);
    }

    private final int scale;
    private final RoundingMode roundingMode;

    public PriceVariationServiceImpl() {
        this.scale          = 1;
        this.roundingMode   = RoundingMode.HALF_UP;

        log.info("price variation initialized with these parameters: scale = {}, rounding mode = {}", scale, roundingMode);
    }
}
