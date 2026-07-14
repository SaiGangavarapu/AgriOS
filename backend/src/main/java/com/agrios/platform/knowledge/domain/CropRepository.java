package com.agrios.platform.knowledge.domain;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CropRepository extends JpaRepository<CropEntity, UUID> {
    Optional<CropEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<CropEntity> findByTenantId(UUID tenantId, Pageable pageable);
    Page<CropEntity> findByTenantIdAndStatus(UUID tenantId, KnowledgeStatus status, Pageable pageable);
}
