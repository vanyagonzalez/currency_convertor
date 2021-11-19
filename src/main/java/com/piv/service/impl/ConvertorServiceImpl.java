package com.piv.service.impl;

import com.piv.dto.ConversionResult;
import com.piv.error.ParameterIsAbsentException;
import com.piv.error.RateIsAbsentException;
import com.piv.service.ConvertorService;
import com.piv.service.RatesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@Service
public class ConvertorServiceImpl implements ConvertorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertorServiceImpl.class);
    public static final String RATE_IS_ABSENT = "A rate is absent for currency [%s]";
    public static final int CENTS_IN_MIDDLE_CURRENCY = 50;
    public static final int CENTS_IN_QUOTE_CURRENCY = 2;
    public static final String A_SOURCE_CURRENCY_IS_ABSENT = "A source currency is absent.";
    public static final String A_TARGET_CURRENCY_IS_ABSENT = "A target currency is absent.";
    public static final String A_MONETARY_VALUE_IS_ABSENT = "A monetary value is absent.";

    private final RatesProvider ratesProvider;

    @Autowired
    public ConvertorServiceImpl(RatesProvider ratesProvider) {
        this.ratesProvider = ratesProvider;
    }

    @Override
    public ConversionResult convert(String from, String to, BigDecimal amount) {
        validate(from, to, amount);

        Map<String, BigDecimal> rates = ratesProvider.getRates(Arrays.asList(from, to));
        LOGGER.info("rates: {}", rates);

        BigDecimal fromRate = getRate(from, rates);
        BigDecimal toRate = getRate(to, rates);
        BigDecimal conversion = amount
                .divide(fromRate, CENTS_IN_MIDDLE_CURRENCY, BigDecimal.ROUND_HALF_UP)
                .multiply(toRate).setScale(CENTS_IN_QUOTE_CURRENCY, BigDecimal.ROUND_HALF_UP);
        return new ConversionResult(from, to, amount, conversion);
    }

    private void validate(String from, String to, BigDecimal amount) {
        if (from == null) {
            throw new ParameterIsAbsentException(A_SOURCE_CURRENCY_IS_ABSENT);
        }
        if (to == null) {
            throw new ParameterIsAbsentException(A_TARGET_CURRENCY_IS_ABSENT);
        }
        if (amount == null) {
            throw new ParameterIsAbsentException(A_MONETARY_VALUE_IS_ABSENT);
        }
    }

    private BigDecimal getRate(String currency, Map<String, BigDecimal> rates) {
        BigDecimal rate = rates.get(currency);
        if (rate == null) {
            throw new RateIsAbsentException(String.format(RATE_IS_ABSENT, currency));
        }
        return rate;
    }
}
