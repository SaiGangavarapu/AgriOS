package com.agrios.platform.ai.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocumentEntity, UUID> {
    Optional<KnowledgeDocumentEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
