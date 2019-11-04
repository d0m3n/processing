package be.better.at.processing.model.jpa.base;

import java.time.Instant;

public interface AuditableJPA {
    Instant getCreatedAt();
    String getCreatedBy();
    Instant getModifiedAt();
    String getModifiedBy();
}
