package dev.matzat.numberconverter.model;

import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public record AuditLogPage(
    List<AuditLogDto> content,
    Pageable pageable,
    Long total
) {

    public AuditLogPage {
        content =  Collections.unmodifiableList(content);
    }
}
