package com.agrios.platform.knowledge.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VarietyRepository extends JpaRepository<VarietyEntity, UUID> {
    Optional<VarietyEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<VarietyEntity> findByTenantIdAndCropId(UUID tenantId, UUID cropId);
}
