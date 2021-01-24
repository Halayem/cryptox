package fr.enix.exchanges.service;

import java.math.BigDecimal;

public interface PriceVariationService {

    boolean arePricesAboutEquals(final BigDecimal price1, final BigDecimal price2);
}
