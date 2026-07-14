package com.agrios.platform.cropplanning.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CropPlanRepository extends JpaRepository<CropPlanEntity, UUID> {
    Optional<CropPlanEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<CropPlanEntity> findByTenantIdAndFieldIdOrderByCreatedAtDesc(
            UUID tenantId, UUID fieldId);
}
