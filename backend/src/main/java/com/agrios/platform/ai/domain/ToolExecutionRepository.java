package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ToolExecutionRepository extends JpaRepository<ToolExecutionEntity, UUID> {
    boolean existsByTenantIdAndIdempotencyKey(UUID tenantId, String idempotencyKey);
    Optional<ToolExecutionEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
