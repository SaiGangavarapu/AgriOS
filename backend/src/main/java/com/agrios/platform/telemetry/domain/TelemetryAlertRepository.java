package com.agrios.platform.telemetry.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TelemetryAlertRepository extends JpaRepository<TelemetryAlertEntity, UUID> {
    List<TelemetryAlertEntity> findByTenantIdAndStatusOrderByTriggeredAtDesc(UUID tenantId, String status);
}
