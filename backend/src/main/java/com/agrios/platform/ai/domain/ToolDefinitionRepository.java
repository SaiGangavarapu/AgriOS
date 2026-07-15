package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ToolDefinitionRepository extends JpaRepository<ToolDefinitionEntity, UUID> {
    Optional<ToolDefinitionEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ToolDefinitionEntity> findByTenantIdAndEnabledTrueOrderByToolCode(UUID tenantId);
}
