package com.agrios.platform.cropcycle.domain;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CropCycleRepository extends JpaRepository<CropCycleEntity, UUID> {
    Optional<CropCycleEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<CropCycleEntity> findByCropPlanIdAndTenantId(UUID planId, UUID tenantId);
    List<CropCycleEntity> findByTenantIdAndFieldIdOrderByCreatedAtDesc(UUID tenantId, UUID fieldId);
}
