package dev.matzat.numberconverter.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @UuidGenerator
    private UUID id;

    @Version
    @Column(updatable = false, nullable = false)
    private Long version;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastModified;

    @CreatedDate
    @Column(nullable = false)
    private Instant created;

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        /*
         When we fetch an entity lazy, Hibernate creates a proxy.
         Since the proxy and the actual object are representing the same entity, we need to consider that here.
         */
        if (Hibernate.getClass(this) != Hibernate.getClass(other) && this.getClass() != other.getClass()) {
            return false;
        }
        return id == ((BaseEntity) other).id;
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    @Override
    public final String toString() {
        // overridden to avoid potential problems with lazy fetching
        return String.format("%s (id=%s, version=%s, created=%s, lastModified=%s), ", this.getClass().getSimpleName(), id, version, created, lastModified);
    }
}
