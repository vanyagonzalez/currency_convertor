package com.piv.controller;

import com.piv.dto.ConversionResult;
import com.piv.error.ApplicationException;
import com.piv.service.ConvertorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class ConvertorController {
    private final ConvertorService convertorService;

    @Autowired
    public ConvertorController(ConvertorService convertorService) {
        this.convertorService = convertorService;
    }

    @GetMapping("/convert")
    public ConversionResult convert(@RequestParam String from,
                                    @RequestParam String to,
                                    @RequestParam BigDecimal amount) {
        try {
            return convertorService.convert(from, to, amount);
        } catch (ApplicationException e) {
            return new ConversionResult(from, to, amount, e.getMessage());
        }
    }

}
