package com.oceanview.resort.strategy;

import java.math.BigDecimal;

public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculateTotal(BigDecimal baseRatePerNight, int nights) {
        if (baseRatePerNight == null) {
            throw new IllegalArgumentException("baseRatePerNight cannot be null");
        }
        if (nights <= 0) {
            throw new IllegalArgumentException("numberOfNights must be > 0");
        }

        // No extra markup yet; pricing rules can be refined later.
        return baseRatePerNight.multiply(BigDecimal.valueOf(nights));
    }
}
