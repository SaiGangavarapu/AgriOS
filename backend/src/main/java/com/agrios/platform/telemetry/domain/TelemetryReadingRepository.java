package com.agrios.platform.telemetry.domain;
import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TelemetryReadingRepository extends JpaRepository<TelemetryReadingEntity, UUID> {
    List<TelemetryReadingEntity> findTop100ByStreamIdOrderByObservedAtDesc(UUID streamId);
    Optional<TelemetryReadingEntity> findTopByStreamIdOrderByObservedAtDesc(UUID streamId);
    List<TelemetryReadingEntity> findByStreamIdAndObservedAtBetween(
            UUID streamId, Instant from, Instant to);
}
