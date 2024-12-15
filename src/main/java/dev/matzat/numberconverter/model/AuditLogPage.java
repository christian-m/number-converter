package dev.matzat.numberconverter.model;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@Getter
public final class AuditLogPage {

    private final List<AuditLogDto> content;
    private final Pageable pageable;
    private final Long total;

    public AuditLogPage(final List<AuditLogDto> content, final PageRequest pageable, final Long total) {
        this.content = Collections.unmodifiableList(content);
        this.pageable = pageable;
        this.total = total;
    }
}
