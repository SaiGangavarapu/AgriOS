package com.agrios.platform.shared;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID eventId();
    Instant occurredAt();
    UUID correlationId();
}
