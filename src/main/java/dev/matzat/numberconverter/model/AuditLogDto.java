package dev.matzat.numberconverter.model;

import java.time.Instant;

public record AuditLogDto(
    Instant timestamp,
    String input,
    String output,
    String statusCode,
    boolean success
) {
}
