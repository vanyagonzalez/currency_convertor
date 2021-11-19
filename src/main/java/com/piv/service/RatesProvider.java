package com.piv.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public interface RatesProvider {
    Map<String, BigDecimal> getRates(Collection<String> currencies);
}
