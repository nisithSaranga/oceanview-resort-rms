package com.oceanview.resort.strategy;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculateTotal(BigDecimal baseRatePerNight, long numberOfNights);
}

