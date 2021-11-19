package com.piv.dto;

import java.math.BigDecimal;

public class ConversionResult {
    private final String from;
    private final String to;
    private final BigDecimal initialAmount;
    private BigDecimal convertedAmount;
    private String error;

    public ConversionResult(String from, String to, BigDecimal initialAmount, BigDecimal convertedAmount) {
        this.from = from;
        this.to = to;
        this.initialAmount = initialAmount;
        this.convertedAmount = convertedAmount;
    }

    public ConversionResult(String from, String to, BigDecimal initialAmount, String error) {
        this.from = from;
        this.to = to;
        this.initialAmount = initialAmount;
        this.error = error;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public String getError() {
        return error;
    }
}
