package com.agrios.platform.integration;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "outbox_event", schema = "integration")
class OutboxEventEntity {
    @Id
    private UUID id;
    private UUID tenantId;
    private String aggregateType;
    private UUID aggregateId;
    private long aggregateVersion;
    private String eventType;
    private int eventVersion;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String payload;
    private UUID correlationId;
    private UUID causationId;
    private Instant occurredAt;
    private Instant publishedAt;
    private int publishAttempts;
    @Column(columnDefinition = "text")
    private String lastError;
    private Instant createdAt;

    protected OutboxEventEntity() {}

    static OutboxEventEntity create(UUID tenantId, String aggregateType, UUID aggregateId,
                                    long aggregateVersion, String eventType, String payload,
                                    UUID correlationId, UUID causationId, Instant occurredAt) {
        OutboxEventEntity entity = new OutboxEventEntity();
        entity.id = UUID.randomUUID();
        entity.tenantId = tenantId;
        entity.aggregateType = aggregateType;
        entity.aggregateId = aggregateId;
        entity.aggregateVersion = aggregateVersion;
        entity.eventType = eventType;
        entity.eventVersion = 1;
        entity.payload = payload;
        entity.correlationId = correlationId;
        entity.causationId = causationId;
        entity.occurredAt = occurredAt;
        entity.createdAt = Instant.now();
        return entity;
    }

    UUID id() {
        return id;
    }
}
