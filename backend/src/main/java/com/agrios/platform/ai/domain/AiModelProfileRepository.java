package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AiModelProfileRepository extends JpaRepository<AiModelProfileEntity, UUID> {
    List<AiModelProfileEntity> findByTenantIdAndModelRoleAndEnabledTrue(UUID tenantId, String modelRole);
}
