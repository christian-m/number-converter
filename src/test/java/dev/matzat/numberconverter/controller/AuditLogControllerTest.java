package dev.matzat.numberconverter.controller;

import dev.matzat.numberconverter.persistence.AuditLog;
import dev.matzat.numberconverter.persistence.AuditLogRepository;
import lombok.AllArgsConstructor;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.List;

@WebFluxTest(AuditLogController.class)
@ExtendWith(MockitoExtension.class)
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AuditLogControllerTest {

    private static final Page<AuditLog> MOCK_AUDIT_LOCK_PAGE = new PageImpl<>(
        List.of(
            new AuditLog(Instant.ofEpochMilli(1734104483000L), "input 1", "output 1", "200 OK", true),
            new AuditLog(Instant.ofEpochMilli(1734104543000L), "input 2", "output 2", "200 OK", true),
            new AuditLog(Instant.ofEpochMilli(1734104603000L), "input 3", "output 3", "422 UNPROCESSABLE_ENTITY", false),
            new AuditLog(Instant.ofEpochMilli(1734104663000L), "input 4", "output 4", "422 UNPROCESSABLE_ENTITY", false)
        ), Pageable.unpaged(), 4);

    private WebTestClient webClient;

    @MockitoBean
    private AuditLogRepository auditLogRepository;

    @Test
    @FlywayTest
    @DisplayName("GIVEN four audit log entries in the database WHEN a audit log page is requested THEN the list with all entries is returned")
    public void testAuditLogPageAll() {
        Mockito.when(auditLogRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(MOCK_AUDIT_LOCK_PAGE);
        webClient.get()
            .uri("/audit-log")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.content[0].timestamp").isEqualTo(Instant.ofEpochMilli(1734104483000L))
            .jsonPath("$.content[0].input").isEqualTo("input 1")
            .jsonPath("$.content[0].output").isEqualTo("output 1")
            .jsonPath("$.content[0].statusCode").isEqualTo("200 OK")
            .jsonPath("$.content[0].success").isEqualTo(true)
            .jsonPath("$.content[1].timestamp").isEqualTo(Instant.ofEpochMilli(1734104543000L))
            .jsonPath("$.content[1].input").isEqualTo("input 2")
            .jsonPath("$.content[1].output").isEqualTo("output 2")
            .jsonPath("$.content[1].statusCode").isEqualTo("200 OK")
            .jsonPath("$.content[1].success").isEqualTo(true)
            .jsonPath("$.content[2].timestamp").isEqualTo(Instant.ofEpochMilli(1734104603000L))
            .jsonPath("$.content[2].input").isEqualTo("input 3")
            .jsonPath("$.content[2].output").isEqualTo("output 3")
            .jsonPath("$.content[2].statusCode").isEqualTo("422 UNPROCESSABLE_ENTITY")
            .jsonPath("$.content[2].success").isEqualTo(false)
            .jsonPath("$.content[3].timestamp").isEqualTo(Instant.ofEpochMilli(1734104663000L))
            .jsonPath("$.content[3].input").isEqualTo("input 4")
            .jsonPath("$.content[3].output").isEqualTo("output 4")
            .jsonPath("$.content[3].statusCode").isEqualTo("422 UNPROCESSABLE_ENTITY")
            .jsonPath("$.content[3].success").isEqualTo(false)
            .jsonPath("$.total").isEqualTo(4)
            .jsonPath("$.pageable.pageNumber").isEqualTo(0)
            .jsonPath("$.pageable.pageSize").isEqualTo(10)
            .jsonPath("$.pageable.offset").isEqualTo(0);
    }
}
