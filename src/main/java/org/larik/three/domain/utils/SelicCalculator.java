package org.larik.three.domain.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Configuration
public class SelicCalculator {

    private static final String SELIC_CONSULT = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.4390/dados/ultimos/1?formato=json";
    private static final String DEFAULT_VALUE = "0.54";

    @Bean
    public BigDecimal selicTaxCalculator() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SELIC_CONSULT))
                    .GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parse(response);

        } catch (Exception e) {
            log.error("BCB API unavailable. Using default value. At: {}", e.getMessage());
            return new BigDecimal(DEFAULT_VALUE);
        }

    }

    private BigDecimal parse(HttpResponse<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        SelicValue[] selics = objectMapper.readValue(response.body(), SelicValue[].class);
        var selic = selics[0];
        return new BigDecimal(selic.value()).divide(new BigDecimal("100"), RoundingMode.HALF_UP);
    }

}

