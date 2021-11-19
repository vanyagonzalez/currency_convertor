package com.piv.dto;

import java.math.BigDecimal;
import java.util.Map;

public class CurrencyRates {
    private boolean success;
    private String base;
    private Map<String, BigDecimal> rates;

    public boolean isSuccess() {
        return success;
    }

    public String getBase() {
        return base;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return "CurrencyRates{" +
                "success=" + success +
                ", base='" + base + '\'' +
                ", rates=" + rates +
                '}';
    }
}
