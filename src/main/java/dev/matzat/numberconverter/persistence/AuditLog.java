package dev.matzat.numberconverter.persistence;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity(name = "audit_log")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuditLog extends BaseEntity {
    private Instant timestamp;
    private String input;
    private String output;
    private String statusCode;
    private boolean success;
}
