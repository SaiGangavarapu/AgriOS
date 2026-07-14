package com.agrios.platform.audit;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AuditEvent(
        UUID id,
        UUID tenantId,
        UUID actorId,
        String actorType,
        String action,
        String targetType,
        UUID targetId,
        Instant occurredAt,
        UUID correlationId,
        String reason,
        Map<String, Object> metadata) {}
