package dev.matzat.numberconverter.controller;

import dev.matzat.numberconverter.converter.BinaryToRomanConverter;
import dev.matzat.numberconverter.converter.ConversionMethod;
import dev.matzat.numberconverter.converter.ConverterResolver;
import dev.matzat.numberconverter.converter.DecimalToRomanConverter;
import dev.matzat.numberconverter.model.ConversionRequest;
import dev.matzat.numberconverter.persistence.AuditLog;
import dev.matzat.numberconverter.persistence.AuditLogRepository;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(ConverterController.class)
@ExtendWith(MockitoExtension.class)
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ConverterControllerTest {

    private WebTestClient webClient;

    @MockitoBean
    private ConverterResolver converterResolver;

    @MockitoBean
    private DecimalToRomanConverter decimalToRomanConverter;

    @MockitoBean
    private BinaryToRomanConverter binaryToRomanConverter;

    @MockitoBean
    private AuditLogRepository auditLogRepository;

    @Test
    @DisplayName("WHEN a valid decimal value is submitted THEN a converted value is returned")
    public void testConversionControllerValidDecimalValue() {
        val givenValue = "15";
        val expectedValue = "XV";
        when(converterResolver.resolve(ConversionMethod.DECIMAL_TO_ROMAN)).thenReturn(decimalToRomanConverter);
        when(decimalToRomanConverter.isValid(givenValue)).thenReturn(true);
        when(decimalToRomanConverter.convert(givenValue)).thenReturn(expectedValue);
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ConversionRequest(ConversionMethod.DECIMAL_TO_ROMAN, givenValue)))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedValue);
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("WHEN an invalid decimal value is submitted THEN the HTTP status 422 is returned")
    public void testConversionControllerInvalidDecimalValue() {
        val givenValue = "XA";
        when(converterResolver.resolve(ConversionMethod.DECIMAL_TO_ROMAN)).thenReturn(decimalToRomanConverter);
        when(decimalToRomanConverter.isValid(givenValue)).thenReturn(false);
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ConversionRequest(ConversionMethod.DECIMAL_TO_ROMAN, givenValue)))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            .expectBody(ProblemDetail.class)
            .consumeWith(response -> {
                val problemDetail = response.getResponseBody();
                assertThat(problemDetail.getDetail()).isEqualTo("convert.conversionRequest.value: The submitted value is not valid for the submitted conversion method");
                assertThat(problemDetail.getStatus()).isEqualTo(422);
            });
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("WHEN a valid binary value is submitted THEN a converted value is returned")
    public void testConversionControllerValidBinaryValue() {
        val givenValue = "1111";
        val expectedValue = "XV";
        when(converterResolver.resolve(ConversionMethod.BINARY_TO_ROMAN)).thenReturn(binaryToRomanConverter);
        when(binaryToRomanConverter.isValid(givenValue)).thenReturn(true);
        when(binaryToRomanConverter.convert(givenValue)).thenReturn(expectedValue);
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ConversionRequest(ConversionMethod.BINARY_TO_ROMAN, givenValue)))
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo(expectedValue);
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("WHEN an invalid binary value is submitted THEN the HTTP status 422 is returned")
    public void testConversionControllerInvalidBinaryValue() {
        val givenValue = "XA";
        when(converterResolver.resolve(ConversionMethod.BINARY_TO_ROMAN)).thenReturn(binaryToRomanConverter);
        when(binaryToRomanConverter.isValid(givenValue)).thenReturn(false);
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ConversionRequest(ConversionMethod.BINARY_TO_ROMAN, givenValue)))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            .expectBody(ProblemDetail.class)
            .consumeWith(response -> {
                val problemDetail = response.getResponseBody();
                assertThat(problemDetail.getDetail()).isEqualTo("convert.conversionRequest.value: The submitted value is not valid for the submitted conversion method");
                assertThat(problemDetail.getStatus()).isEqualTo(422);
            });
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("WHEN an invalid conversion method is submitted THEN the HTTP status 400 is returned")
    public void testConversionControllerInvalidConversionMethod() {
        val givenValue = "15";
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());
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
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("WHEN an conversion method is submitted that have no converter implemented THEN the HTTP status 422 is returned")
    public void testConversionControllerConversionMethodNotFound() {
        val givenValue = "15";
        when(converterResolver.resolve(ConversionMethod.DECIMAL_TO_ROMAN)).thenThrow(NoSuchElementException.class);
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ConversionRequest(ConversionMethod.DECIMAL_TO_ROMAN, givenValue)))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            .expectBody(ProblemDetail.class)
            .consumeWith(response -> {
                val problemDetail = response.getResponseBody();
                assertThat(problemDetail.getDetail()).isEqualTo("convert.conversionRequest.conversionMethod: No converter found for conversion method");
                assertThat(problemDetail.getStatus()).isEqualTo(422);
            });
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("WHEN an unexpected exception occurs THEN the HTTP status 500 is returned")
    public void testConversionControllerInternalServerError() {
        val givenValue = "15";
        when(converterResolver.resolve(ConversionMethod.DECIMAL_TO_ROMAN)).thenThrow(RuntimeException.class);
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(new AuditLog());
        webClient.post()
            .uri("/convert")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ConversionRequest(ConversionMethod.DECIMAL_TO_ROMAN, givenValue)))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
            .expectBody(ProblemDetail.class)
            .consumeWith(response -> {
                val problemDetail = response.getResponseBody();
                assertThat(problemDetail.getDetail()).isEqualTo("An unexpected error occurred");
                assertThat(problemDetail.getStatus()).isEqualTo(500);
            });
        verify(auditLogRepository).save(any(AuditLog.class));
    }
}
