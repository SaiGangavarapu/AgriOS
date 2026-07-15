package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface KnowledgeSourceRepository extends JpaRepository<KnowledgeSourceEntity, UUID> {
    Optional<KnowledgeSourceEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
