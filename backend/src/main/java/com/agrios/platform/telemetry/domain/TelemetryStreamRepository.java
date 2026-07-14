package com.agrios.platform.telemetry.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TelemetryStreamRepository extends JpaRepository<TelemetryStreamEntity, UUID> {
    Optional<TelemetryStreamEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<TelemetryStreamEntity> findByTenantIdAndDeviceIdAndStatus(
            UUID tenantId, UUID deviceId, String status);
}
