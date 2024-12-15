package dev.matzat.numberconverter.controller;

import dev.matzat.numberconverter.model.AuditLogDto;
import dev.matzat.numberconverter.model.AuditLogPage;
import dev.matzat.numberconverter.persistence.AuditLogRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit-log")
@AllArgsConstructor
@SuppressWarnings("DesignForExtension")
public class AuditLogController {

    private AuditLogRepository auditLogRepository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponse(
        responseCode = "200",
        description = "A page of audit logs")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuditLogPage> listAuditLogs(
        @RequestParam(defaultValue = "0") final int page,
        @RequestParam(defaultValue = "10") final int size,
        @RequestParam(defaultValue = "created") final String sort
    ) {
        val pageable = PageRequest.of(page, size, Sort.by(sort));
        val auditLogs = auditLogRepository.findAll(pageable);
        val auditLogDtos = auditLogs.stream().map(auditLog ->
            new AuditLogDto(auditLog.getTimestamp(), auditLog.getInput(), auditLog.getOutput(), auditLog.getStatusCode(), auditLog.isSuccess())).toList();
        return ResponseEntity.ok().body(new AuditLogPage(auditLogDtos, pageable, auditLogs.getTotalElements()));
    }
}
