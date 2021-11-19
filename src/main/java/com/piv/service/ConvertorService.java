package com.piv.service;

import com.piv.dto.ConversionResult;

import java.math.BigDecimal;

public interface ConvertorService {
    ConversionResult convert(String from, String to, BigDecimal amount);
}
