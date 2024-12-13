package dev.matzat.numberconverter.controller;

import dev.matzat.numberconverter.converter.ConversionMethod;
import dev.matzat.numberconverter.model.ConversionRequest;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ConverterControllerIntegrationTest {

    private WebTestClient webClient;

    @Test
    @DisplayName("WHEN a valid decimal value is submitted THEN a converted value is returned")
    public void testConversionControllerValidDecimalValue() {
        val givenValue = "15";
        val expectedValue = "XV";
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ConversionRequest(ConversionMethod.DECIMAL_TO_ROMAN, givenValue)))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("WHEN an invalid decimal value is submitted THEN the HTTP status 422 is returned")
    public void testConversionControllerInvalidDecimalValue() {
        val givenValue = "XA";
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("{\"conversionMethod\":\"DECIMAL_TO_ROMAN\",\"value\":\"" + givenValue + "\"}"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            .expectBody(ProblemDetail.class)
            .consumeWith(response -> {
                val problemDetail = response.getResponseBody();
                assertThat(problemDetail.getDetail()).isEqualTo(String.format("Submitted input '%s' is not a valid decimal number value", givenValue));
                assertThat(problemDetail.getStatus()).isEqualTo(422);
            });
    }

    @Test
    @DisplayName("WHEN a valid binary value is submitted THEN a converted value is returned")
    public void testConversionControllerValidBinaryValue() {
        val givenValue = "1111";
        val expectedValue = "XV";
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ConversionRequest(ConversionMethod.BINARY_TO_ROMAN, givenValue)))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("WHEN an invalid binary value is submitted THEN the HTTP status 422 is returned")
    public void testConversionControllerInvalidBinaryValue() {
        val givenValue = "XA";
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("{\"conversionMethod\":\"BINARY_TO_ROMAN\",\"value\":\"" + givenValue + "\"}"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            .expectBody(ProblemDetail.class)
            .consumeWith(response -> {
                val problemDetail = response.getResponseBody();
                assertThat(problemDetail.getDetail()).isEqualTo(String.format("Submitted input '%s' is not a valid binary value", givenValue));
                assertThat(problemDetail.getStatus()).isEqualTo(422);
            });
    }

    @Test
    @DisplayName("WHEN an invalid conversion method is submitted THEN the HTTP status 400 is returned")
    public void testConversionControllerInvalidConversionMethod() {
        val givenValue = "15";
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("{\"conversionMethod\":\"INVALID\",\"value\":\"" + givenValue + "\"}"))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
            .expectBody(ProblemDetail.class)
            .consumeWith(response -> {
                val problemDetail = response.getResponseBody();
                assertThat(problemDetail.getDetail()).isEqualTo("400 BAD_REQUEST \"Failed to read HTTP message\"");
                assertThat(problemDetail.getStatus()).isEqualTo(400);
            });
    }
}
