package fr.enix.common.utils.math;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationMathUtils {

    private static final int SCALE = 8;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.DOWN;

    public static BigDecimal doDivision(final BigDecimal dividend, final BigDecimal divisor) {
        return dividend.divide(divisor, SCALE, ROUNDING_MODE);
    }
}
