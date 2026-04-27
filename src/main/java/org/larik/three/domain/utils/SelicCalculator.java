package org.larik.three.domain.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class SelicCalculator {

    private static final String SELIC_CONSULT = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.4390/dados/ultimos/1?formato=json";

    @Bean
    public BigDecimal selicTaxCalculator() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SELIC_CONSULT))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        SelicValue [] selics = objectMapper.readValue(response.body(), SelicValue[].class);
        var selic = selics[0];
        return new BigDecimal(selic.value()).divide(new BigDecimal("100"), RoundingMode.HALF_UP);
    }

}

