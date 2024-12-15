package dev.matzat.numberconverter.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AuditLogRepository extends PagingAndSortingRepository<AuditLog, UUID>, CrudRepository<AuditLog, UUID> {
}
