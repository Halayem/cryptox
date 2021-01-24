package fr.enix.exchanges.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PriceVariationServiceTest {

    @Autowired
    private PriceVariationService priceVariationService;

    @Test
    void testArePricesAboutEquals_shouldBeEquals() {
        assertTrue( priceVariationService.arePricesAboutEquals(new BigDecimal("1.42999999"  ), new BigDecimal("1.4" )) );
        assertTrue( priceVariationService.arePricesAboutEquals(new BigDecimal("1.44999999"  ), new BigDecimal("1.4" )) );
        assertTrue( priceVariationService.arePricesAboutEquals(new BigDecimal("1.42"        ), new BigDecimal("1.4" )) );

        assertTrue( priceVariationService.arePricesAboutEquals(new BigDecimal("1.45"        ), new BigDecimal("1.5" )) );
        assertTrue( priceVariationService.arePricesAboutEquals(new BigDecimal("1.45000001"  ), new BigDecimal("1.49845" )) );
        assertTrue( priceVariationService.arePricesAboutEquals(new BigDecimal("1.49999999"  ), new BigDecimal("1.5" )) );
        assertTrue( priceVariationService.arePricesAboutEquals(new BigDecimal("1.54999999"  ), new BigDecimal("1.5" )) );

        assertTrue( priceVariationService.arePricesAboutEquals(new BigDecimal("1.55000001"  ), new BigDecimal("1.5989432" )) );
    }

    @Test
    void testArePricesAboutEquals_shouldNotBeEquals() {
        assertFalse( priceVariationService.arePricesAboutEquals(new BigDecimal("1.42999999"  ), new BigDecimal("1.51999999" )) );
        assertFalse( priceVariationService.arePricesAboutEquals(new BigDecimal("1.55000001"  ), new BigDecimal("0.55000001" )) );
    }

    @Test
    void testArePricesAboutEquals_shouldNotBeEqualsForNullArgument() {
        assertFalse( priceVariationService.arePricesAboutEquals( null, new BigDecimal("1.51999999" ) ));
        assertFalse( priceVariationService.arePricesAboutEquals( new BigDecimal("1.55000001" ), null ));
        assertFalse( priceVariationService.arePricesAboutEquals( null, null ));
    }
}
