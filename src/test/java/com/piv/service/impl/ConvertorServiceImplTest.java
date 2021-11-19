package com.piv.service.impl;

import com.piv.dto.ConversionResult;
import com.piv.error.ParameterIsAbsentException;
import com.piv.error.RateIsAbsentException;
import com.piv.service.ConvertorService;
import com.piv.service.RatesProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConvertorServiceImplTest {
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String WRONG = "wrong";
    public static final BigDecimal AMOUNT = BigDecimal.ONE;
    private ConvertorService convertorService;

    @Before
    public void init () {
        RatesProvider ratesProvider = Mockito.mock(RatesProvider.class);
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(FROM, BigDecimal.ONE);
        rates.put(TO, BigDecimal.TEN);
        Mockito.when(ratesProvider.getRates(Arrays.asList(FROM, TO))).thenReturn(rates);
        Mockito.when(ratesProvider.getRates(Arrays.asList(WRONG, TO))).thenReturn(rates);
        Mockito.when(ratesProvider.getRates(Arrays.asList(FROM, WRONG))).thenReturn(rates);
        convertorService = new ConvertorServiceImpl(ratesProvider);
    }

    @Test
    public void shouldThrowExceptionIfFromCurrencyIsAbsent() {
        try {
            convertorService.convert(null, TO, AMOUNT);
            Assert.fail();
        } catch (ParameterIsAbsentException e) {
            Assert.assertEquals("A source currency is absent.", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionIfToCurrencyIsAbsent() {
        try {
            convertorService.convert(FROM, null, AMOUNT);
            Assert.fail();
        } catch (ParameterIsAbsentException e) {
            Assert.assertEquals("A target currency is absent.", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionIfAmountIsAbsent() {
        try {
            convertorService.convert(FROM, TO, null);
            Assert.fail();
        } catch (ParameterIsAbsentException e) {
            Assert.assertEquals("A monetary value is absent.", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionIfFromRateIsAbsetn() {
        try {
            convertorService.convert(WRONG, TO, AMOUNT);
            Assert.fail();
        } catch (RateIsAbsentException e) {
            Assert.assertEquals("A rate is absent for currency [wrong]", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionIfToRateIsAbsetn() {
        try {
            convertorService.convert(FROM, WRONG, AMOUNT);
            Assert.fail();
        } catch (RateIsAbsentException e) {
            Assert.assertEquals("A rate is absent for currency [wrong]", e.getMessage());
        }
    }

    @Test
    public void shouldReturnCorrectResultIfAllIsCorrect() {
        ConversionResult result = convertorService.convert(FROM, TO, AMOUNT);
        Assert.assertEquals(FROM, result.getFrom());
        Assert.assertEquals(TO, result.getTo());
        Assert.assertEquals(AMOUNT, result.getInitialAmount());
        Assert.assertEquals(0, BigDecimal.TEN.compareTo(result.getConvertedAmount()));
    }
}