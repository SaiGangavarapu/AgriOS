package com.agrios.platform.integration;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxService {
    private final OutboxEventRepository repository;

    OutboxService(OutboxEventRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UUID append(UUID tenantId, String aggregateType, UUID aggregateId,
                       long aggregateVersion, String eventType, String payload,
                       UUID correlationId, UUID causationId, Instant occurredAt) {
        OutboxEventEntity event = OutboxEventEntity.create(
                tenantId, aggregateType, aggregateId, aggregateVersion,
                eventType, payload, correlationId, causationId, occurredAt);
        repository.save(event);
        return event.id();
    }
}
