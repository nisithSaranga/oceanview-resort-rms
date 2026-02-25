package com.oceanview.resort.strategy;

import java.math.BigDecimal;

public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculateTotal(BigDecimal baseRatePerNight, long numberOfNights) {
        if (baseRatePerNight == null) {
            throw new IllegalArgumentException("baseRatePerNight cannot be null");
        }
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("numberOfNights must be > 0");
        }

        // No extra markup yet; pricing rules can be refined later.
        return baseRatePerNight.multiply(BigDecimal.valueOf(numberOfNights));
    }
}
