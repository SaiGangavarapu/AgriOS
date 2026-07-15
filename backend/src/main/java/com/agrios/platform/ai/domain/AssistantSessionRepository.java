package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AssistantSessionRepository extends JpaRepository<AssistantSessionEntity, UUID> {
    Optional<AssistantSessionEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
