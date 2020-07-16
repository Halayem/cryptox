package fr.enix.exchanges.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssetMinimumOrder {

    public static final BigDecimal LITECOIN = new BigDecimal("0.1"  );
    public static final BigDecimal EURO     = new BigDecimal("10"   );
}
