package com.agrios.platform.irrigation.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ActuatorCommandRepository extends JpaRepository<ActuatorCommandEntity, UUID> {
    boolean existsByTenantIdAndIdempotencyKey(UUID tenantId, String idempotencyKey);
}
