package dev.matzat.numberconverter.controller;

import dev.matzat.numberconverter.SpringBootTestBase;
import dev.matzat.numberconverter.converter.ConversionMethod;
import dev.matzat.numberconverter.model.ConversionRequest;
import dev.matzat.numberconverter.persistence.AuditLogRepository;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@AllArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
public class ConverterControllerIntegrationTest extends SpringBootTestBase {

    private WebTestClient webClient;

    private AuditLogRepository auditLogRepository;

    @BeforeEach
    void setUp() {
        auditLogRepository.deleteAll();
    }

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
        val auditLogs = auditLogRepository.findAll();
        assertThat(auditLogs).hasSize(1);
        val auditLog = auditLogs.iterator().next();
        assertThat(auditLog.getInput()).isEqualTo("{\"conversionMethod\":\"DECIMAL_TO_ROMAN\",\"value\":\"15\"}");
        assertThat(auditLog.getOutput()).isEqualTo("XV");
        assertThat(auditLog.getStatusCode()).isEqualTo("200 OK");
        assertThat(auditLog.isSuccess()).isTrue();
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
                assertThat(problemDetail.getDetail()).isEqualTo("convert.conversionRequest.value: The submitted value is not valid for the submitted conversion method");
                assertThat(problemDetail.getStatus()).isEqualTo(422);
            });
        val auditLogs = auditLogRepository.findAll();
        assertThat(auditLogs).hasSize(1);
        val auditLog = auditLogs.iterator().next();
        assertThat(auditLog.getInput()).isEqualTo("{\"conversionMethod\":\"DECIMAL_TO_ROMAN\",\"value\":\"XA\"}");
        assertThat(auditLog.getOutput()).isEqualTo("{\"type\":\"about:blank\",\"title\":\"Unprocessable Entity\",\"status\":422,\"detail\":\"convert.conversionRequest.value: The submitted value is not valid for the submitted conversion method\",\"instance\":\"/convert\",\"fieldErrorDetails\":{\"convert.conversionRequest.value\":[\"The submitted value is not valid for the submitted conversion method\"]}}");
        assertThat(auditLog.getStatusCode()).isEqualTo("422 UNPROCESSABLE_ENTITY");
        assertThat(auditLog.isSuccess()).isFalse();
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
        val auditLogs = auditLogRepository.findAll();
        assertThat(auditLogs).hasSize(1);
        val auditLog = auditLogs.iterator().next();
        assertThat(auditLog.getInput()).isEqualTo("{\"conversionMethod\":\"BINARY_TO_ROMAN\",\"value\":\"1111\"}");
        assertThat(auditLog.getOutput()).isEqualTo("XV");
        assertThat(auditLog.getStatusCode()).isEqualTo("200 OK");
        assertThat(auditLog.isSuccess()).isTrue();
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
                assertThat(problemDetail.getDetail()).isEqualTo("convert.conversionRequest.value: The submitted value is not valid for the submitted conversion method");
                assertThat(problemDetail.getStatus()).isEqualTo(422);
            });
        val auditLogs = auditLogRepository.findAll();
        assertThat(auditLogs).hasSize(1);
        val auditLog = auditLogs.iterator().next();
        assertThat(auditLog.getInput()).isEqualTo("{\"conversionMethod\":\"BINARY_TO_ROMAN\",\"value\":\"XA\"}");
        assertThat(auditLog.getOutput()).isEqualTo("{\"type\":\"about:blank\",\"title\":\"Unprocessable Entity\",\"status\":422,\"detail\":\"convert.conversionRequest.value: The submitted value is not valid for the submitted conversion method\",\"instance\":\"/convert\",\"fieldErrorDetails\":{\"convert.conversionRequest.value\":[\"The submitted value is not valid for the submitted conversion method\"]}}");
        assertThat(auditLog.getStatusCode()).isEqualTo("422 UNPROCESSABLE_ENTITY");
        assertThat(auditLog.isSuccess()).isFalse();
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
        val auditLogs = auditLogRepository.findAll();
        assertThat(auditLogs).hasSize(1);
        val auditLog = auditLogs.iterator().next();
        assertThat(auditLog.getInput()).isEqualTo("{\"conversionMethod\":\"INVALID\",\"value\":\"15\"}");
        assertThat(auditLog.getOutput()).isEqualTo("{\"type\":\"about:blank\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"400 BAD_REQUEST \\\"Failed to read HTTP message\\\"\",\"instance\":\"/convert\"}");
        assertThat(auditLog.getStatusCode()).isEqualTo("400 BAD_REQUEST");
        assertThat(auditLog.isSuccess()).isFalse();
    }
}
