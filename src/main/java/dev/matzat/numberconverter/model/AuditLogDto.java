package dev.matzat.numberconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class AuditLogDto {
    private Instant timestamp;
    private String input;
    private String output;
    private String statusCode;
    private boolean success;
}
