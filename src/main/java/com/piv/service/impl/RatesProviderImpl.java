package com.piv.service.impl;

import com.piv.dto.CurrencyRates;
import com.piv.error.SomethingWasWrongException;
import com.piv.service.RatesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class RatesProviderImpl implements RatesProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatesProviderImpl.class);
    private static final String URL = "http://api.exchangeratesapi.io/v1/latest";

    @Value("${access_key}")
    private String accessKey;

    private final WebClient webClient = WebClient.create(URL);

    @Override
    public Map<String, BigDecimal> getRates(final Collection<String> currencies) {
        CurrencyRates currencyRates = webClient.get()
                .uri("?access_key={accessKey}", accessKey)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new SomethingWasWrongException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new SomethingWasWrongException("Server is not responding")))
                .bodyToMono(CurrencyRates.class)
                .block();
        LOGGER.info("currencyRates: {}", currencyRates);

        if (currencyRates == null) {
            throw new SomethingWasWrongException("Rates are absent");
        }

        final Map<String, BigDecimal> result = new HashMap<>();
        currencies.forEach(currency -> result.put(currency, currencyRates.getRates().get(currency)));
        return result;
    }
}
